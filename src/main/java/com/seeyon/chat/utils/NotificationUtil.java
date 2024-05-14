package com.seeyon.chat.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * @author Shaozz
 */
public class NotificationUtil {

    public static void error(String content) {
        Notification notification = new Notification(getGroupId(), content, NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static String getGroupId() {
        return ChatBundle.message("notification.group.id");
    }

}
