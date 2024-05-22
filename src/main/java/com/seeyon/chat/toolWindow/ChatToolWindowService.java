package com.seeyon.chat.toolWindow;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.util.text.Strings;
import com.seeyon.chat.listener.ChatMessageSubscriber;
import com.seeyon.chat.settings.AppSettingsConfigurable;
import com.seeyon.chat.settings.AppSettingsState;
import com.seeyon.chat.ui.ChatBoxComponent;
import com.seeyon.chat.ui.ChatComponent;
import com.seeyon.chat.utils.ChatHttpUtil;
import com.seeyon.chat.utils.NotificationUtil;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Shaozz
 */
@Service(Service.Level.PROJECT)
public final class ChatToolWindowService {

    private final ChatToolWindow chatToolWindow;

    private final AtomicBoolean lock;

    private CompletableFuture<HttpResponse<Void>> future;

    public ChatToolWindowService() {
        this.chatToolWindow = new ChatToolWindow();
        this.lock = new AtomicBoolean();
    }

    public static ChatToolWindowService getInstance() {
        return ApplicationManager.getApplication().getService(ChatToolWindowService.class);
    }

    public ChatToolWindow getChatToolWindow() {
        return chatToolWindow;
    }

    public boolean isLocked() {
        return lock.get();
    }

    public void unlock() {
        lock.set(false);
    }

    public CompletableFuture<HttpResponse<Void>> getFuture() {
        return future;
    }

    public void actionPerformed(String data) {
        if (data.isEmpty()) {
            return;
        }
        // check settings
        if (!checkSettings()) {
            return;
        }
        if (!lock.compareAndSet(false, true)) {
            return;
        }

        // add the conversation component to chatBox
        ChatBoxComponent chatBoxComponent = chatToolWindow.getChatBoxComponent();
        chatBoxComponent.addChat(ChatComponent.ofQuestion(data).getComponent());

        // before send
        chatToolWindow.aroundSend(false);

        // send message
        try {
            future = ChatHttpUtil.sendMessage(getChatId(), data, new ChatMessageSubscriber(chatBoxComponent));
        } catch (Exception e) {
            NotificationUtil.error(e.getMessage());

            stopGenerating();
        }
    }

    public void stopGenerating() {
        chatToolWindow.getSearchBoxComponent().getStopButton().doClick();
    }

    public void createNewChat() throws IOException, InterruptedException {
        // stop generating
        stopGenerating();

        // clear chats
        chatToolWindow.getChatBoxComponent().removeAllChats();

        // create new chatId
        AppSettingsState settings = AppSettingsState.getInstance();
        if (Strings.isEmpty(settings.getApiKey())) {
            settings.setChatId(null);
        } else {
            settings.setChatId(ChatHttpUtil.createChat(settings.getChatbotId()));
        }
    }

    private boolean checkSettings() {
        AppSettingsState settings = AppSettingsState.getInstance();
        if (Strings.isEmpty(settings.getApiKey())) {
            Notification notification = new Notification(NotificationUtil.getGroupId(), "Missing API Key", NotificationType.WARNING);
            notification.addAction(NotificationAction.create("Configure...", e -> ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), AppSettingsConfigurable.class)));
            Notifications.Bus.notify(notification);
            return false;
        }
        return true;
    }

    private static String getChatId() throws Exception {
        AppSettingsState settings = AppSettingsState.getInstance();
        String chatId = settings.getChatId();
        if (Strings.isEmpty(chatId)) {
            try {
                chatId = ChatHttpUtil.createChat(settings.getChatbotId());
                settings.setChatId(chatId);
            } catch (RuntimeException e) {
                if ("Chatbot not found.".equals(e.getMessage())) {
                    // create chatbot
                    String chatbotId = ChatHttpUtil.createChatbot(settings.findModel());
                    settings.putChatbotId(chatbotId);

                    chatId = ChatHttpUtil.createChat(chatbotId);
                    settings.setChatId(chatId);
                } else {
                    throw e;
                }
            }
        }
        return chatId;
    }
}
