package com.seeyon.chat.toolWindow.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.seeyon.chat.toolWindow.ChatToolWindowService;
import com.seeyon.chat.utils.NotificationUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Shaozz
 */
public class NewChatAction extends DumbAwareAction {

    public NewChatAction(String text, Icon icon) {
        super(() -> text, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            ChatToolWindowService.getInstance().createNewChat();
        } catch (Exception ex) {
            NotificationUtil.error(ex.getMessage());
        }
    }
}
