package com.seeyon.chat.core.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.seeyon.chat.core.ChatMessageSubscriber;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.settings.AppSettingsState;
import com.seeyon.chat.toolWindow.ChatToolWindowContent;
import com.seeyon.chat.ui.ChatPanel;
import com.seeyon.chat.ui.ChatCell;
import com.seeyon.chat.utils.ChatHttpUtil;
import com.seeyon.chat.utils.ChatSettingsUtil;
import com.seeyon.chat.utils.NotificationUtil;

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

    private CompletableFuture<HttpResponse<Void>> future;

    public ChatService() {
        this.chatToolWindowContent = new ChatToolWindowContent();
        this.lock = new AtomicBoolean();
    }

    public static ChatService getInstance() {
        return ApplicationManager.getApplication().getService(ChatService.class);
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

    public void actionPerformed() {
        String text = chatToolWindowContent.getSearchBoxComponent().getTextArea().getText();
        actionPerformed(text);
    }

    public void actionPerformed(String data) {
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
            future = ChatHttpUtil.sendMessage(ChatSettingsUtil.getChatId(), data, new ChatMessageSubscriber(chatPanel));
        } catch (Exception e) {
            stopGenerating();
            NotificationUtil.error(e.getMessage());
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
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.setChatId(ChatHttpUtil.createChat(settings.getChatbotId()));
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
    }
}
