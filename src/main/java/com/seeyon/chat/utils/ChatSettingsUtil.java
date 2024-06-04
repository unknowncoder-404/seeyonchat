package com.seeyon.chat.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.util.text.Strings;
import com.seeyon.chat.settings.AppSettingsConfigurable;
import com.seeyon.chat.settings.AppSettingsState;

/**
 * @author Shaozz
 */
public class ChatSettingsUtil {

    public static boolean checkSettings() {
        AppSettingsState settings = AppSettingsState.getInstance();
        if (Strings.isEmpty(settings.getApiKey())) {
            Notification notification = new Notification(NotificationUtil.getGroupId(), "Missing API Key", NotificationType.WARNING);
            notification.addAction(NotificationAction.create("Configure...", e -> ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), AppSettingsConfigurable.class)));
            Notifications.Bus.notify(notification);
            return false;
        }
        return true;
    }

    public static String getChatId() throws Exception {
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
