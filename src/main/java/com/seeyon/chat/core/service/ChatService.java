package com.seeyon.chat.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.Strings;
import com.seeyon.chat.common.ChatConstants;
import com.seeyon.chat.core.ChatMessageSubscriber;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.settings.AppSettingsState;
import com.seeyon.chat.toolWindow.ChatToolWindowContent;
import com.seeyon.chat.ui.ChatPanel;
import com.seeyon.chat.ui.ChatCell;
import com.seeyon.chat.utils.ChatHttpUtil;
import com.seeyon.chat.utils.ChatSettingsUtil;
import com.seeyon.chat.utils.NotificationUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Shaozz
 */
@Service(Service.Level.PROJECT)
public final class ChatService {

    private final ChatToolWindowContent chatToolWindowContent;

    private final AtomicBoolean lock;

    private volatile String chatId;

    private CompletableFuture<HttpResponse<Void>> future;

    public ChatService(Project project) {
        this.chatToolWindowContent = new ChatToolWindowContent(project);
        this.lock = new AtomicBoolean();
    }

    public static ChatService getInstance(@NotNull Project project) {
        return project.getService(ChatService.class);
    }

    public ChatToolWindowContent getToolWindowContent() {
        return chatToolWindowContent;
    }

    public boolean isLocked() {
        return lock.get();
    }

    public void unlock() {
        lock.set(false);
    }

    public String getChatId() {
        return chatId;
    }

    public void sendMessage(String data) {
        // check
        if (!ChatSettingsUtil.checkSettings() || !lock.compareAndSet(false, true)) {
            return;
        }

        // add the chat component to chatPanel
        ChatPanel chatPanel = chatToolWindowContent.getChatPanel();
        if (!data.isEmpty()) {
            chatPanel.addChatCell(ChatCell.ofAsk(data));
        }

        // before send
        chatToolWindowContent.aroundSend(false);

        // send message
        boolean isNewChat = false;
        CompletableFuture<Void> completion = new CompletableFuture<>();
        try {
            if (Strings.isEmpty(chatId)) {
                chatId = createChat();
                isNewChat = true;
            }
            ChatMessageSubscriber subscriber = new ChatMessageSubscriber(chatPanel, completion);
            future = ChatHttpUtil.sendMessage(chatId, data, subscriber);
            completion = completion.thenRun(this::stopGenerating)
                    .exceptionally(ex -> {
                        stopGenerating();
                        SwingUtilities.invokeLater(() -> NotificationUtil.error(ex.getMessage()));
                        return null;
                    });
            if (isNewChat) {
                completion = completion.thenCompose(response -> ChatHttpUtil.getSummarizeAsync(chatId))
                        .thenAccept(response -> {
                            try {
                                JsonNode jsonNode = ChatConstants.OBJECT_MAPPER.readTree(response.body());
                                if (jsonNode.has("summary")) {
                                    ObjectNode bodyJson = ChatConstants.OBJECT_MAPPER.createObjectNode();
                                    bodyJson.put("title", jsonNode.get("summary").asText());
                                    ChatHttpUtil.updateChatAsync(chatId, ChatConstants.OBJECT_MAPPER.writeValueAsString(bodyJson));
                                }
                            } catch (Exception ignored) {
                            }
                        });
            }
        } catch (Exception e) {
            stopGenerating();
            completion.cancel(true);
            NotificationUtil.error(e.getMessage());
        }
    }

    private String createChat() throws IOException, InterruptedException {
        AppSettingsState settings = AppSettingsState.getInstance();
        String chatbotId = settings.getChatbotId();
        if (Strings.isEmpty(chatbotId)) {
            chatbotId = ChatHttpUtil.createChatbot(settings.getModel());
            settings.putChatbotId(chatbotId);
        }
        try {
            return ChatHttpUtil.createChat(chatbotId);
        } catch (RuntimeException e) {
            // create chatbot
            chatbotId = ChatHttpUtil.createChatbot(settings.getModel());
            settings.putChatbotId(chatbotId);

            return ChatHttpUtil.createChat(chatbotId);
        }
    }

    public void stopGenerating() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        SwingUtilities.invokeLater(() -> chatToolWindowContent.aroundSend(true));
        unlock();
    }

    public void clearCurrentChat() {
        // stop generating
        stopGenerating();

        // clear chats
        SwingUtilities.invokeLater(() -> chatToolWindowContent.getChatPanel().removeChat());

        // clear chatId
        chatId = null;
    }

    public void recoverChat(String chatId) throws IOException, InterruptedException {
        Chat chat = ChatHttpUtil.getChat(chatId);
        List<ChatCell> chatCells = chat.getMessages().stream().map(message -> {
            if (message.getRole().equals("user")) {
                return ChatCell.ofAsk(message.getContent());
            } else {
                return ChatCell.ofAnswer(message.getContent());
            }
        }).toList();
        chatToolWindowContent.getChatPanel().replaceChat(chatCells);

        this.chatId = chatId;
    }

    public void regenerate() throws IOException, InterruptedException {
        ChatPanel chatPanel = chatToolWindowContent.getChatPanel();
        chatPanel.removeRegenerateLabel();
        // Delete the latest answer cell
        ChatCell chatCell = chatPanel.getLatestChatCell();
        if (!(chatCell == null || chatCell.isAsk())) {
            chatPanel.removeLatestChatCell();
        }
        ChatHttpUtil.trimMessages(this.chatId, "{\"match\":{\"role\":\"user\"}}");
        this.sendMessage("");
    }
}
