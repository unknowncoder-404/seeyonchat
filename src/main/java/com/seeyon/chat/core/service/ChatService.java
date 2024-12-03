package com.seeyon.chat.core.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.Strings;
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

    private final Project project;

    private final ChatToolWindowContent chatToolWindowContent;

    private final AtomicBoolean lock;

    private volatile String chatId;

    private CompletableFuture<HttpResponse<Void>> future;

    public ChatService(Project project) {
        this.project = project;

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

    public void sendMessage() {
        String text = chatToolWindowContent.getSearchBoxComponent().getTextArea().getText();
        sendMessage(text);
    }

    public void sendMessage(String data) {
        if (data.isEmpty()) {
            return;
        }
        // check settings
        if (!ChatSettingsUtil.checkSettings()) {
            return;
        }
        if (!lock.compareAndSet(false, true)) {
            return;
        }

        // add the conversation component to chatBox
        ChatPanel chatPanel = chatToolWindowContent.getChatPanel();
        chatPanel.addChat(ChatCell.ofAsk(data));

        // before send
        chatToolWindowContent.aroundSend(false);

        // send message
        try {
            if (Strings.isEmpty(chatId)) {
                chatId = createChat();
            }
            future = ChatHttpUtil.sendMessage(chatId, data, new ChatMessageSubscriber(chatPanel, project));
        } catch (Exception e) {
            stopGenerating();
            NotificationUtil.error(e.getMessage());
        }
    }

    private String createChat() throws IOException, InterruptedException {
        AppSettingsState settings = AppSettingsState.getInstance();
        String chatbotId = settings.getChatbotId();
        if (Strings.isEmpty(chatbotId)) {
            chatbotId = ChatHttpUtil.createChatbot(settings.findModel());
            settings.putChatbotId(chatbotId);
        }
        try {
            return ChatHttpUtil.createChat(chatbotId);
        } catch (RuntimeException e) {
            if ("Chatbot not found.".equals(e.getMessage())) {
                // create chatbot
                chatbotId = ChatHttpUtil.createChatbot(settings.findModel());
                settings.putChatbotId(chatbotId);

                return ChatHttpUtil.createChat(chatbotId);
            } else {
                throw e;
            }
        }
    }

    public void stopGenerating() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        SwingUtilities.invokeLater(() -> chatToolWindowContent.aroundSend(true));
        unlock();
    }

    public void createNewChat() throws IOException, InterruptedException {
        if (!ChatSettingsUtil.checkSettings()) {
            return;
        }
        // stop generating
        stopGenerating();

        // clear chats
        chatToolWindowContent.getChatPanel().removeAllChats();

        // create new chatId
        chatId = createChat();
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
        chatToolWindowContent.getChatPanel().replaceAllChats(chatCells);

        this.chatId = chatId;
    }
}
