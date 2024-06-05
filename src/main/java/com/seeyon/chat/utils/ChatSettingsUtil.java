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

}
