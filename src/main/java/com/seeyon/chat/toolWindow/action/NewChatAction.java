package com.seeyon.chat.toolWindow.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.utils.NotificationUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Shaozz
 */
public class NewChatAction extends DumbAwareAction {

    private final ContentManager contentManager;
    private final Content chatContent;

    public NewChatAction(String text, Icon icon, ContentManager contentManager, Content chatContent) {
        super(() -> text, icon);
        this.contentManager = contentManager;
        this.chatContent = chatContent;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (!contentManager.isSelected(chatContent)) {
            contentManager.setSelectedContent(chatContent);
        }
        try {
            ChatService.getInstance(e.getProject()).createNewChat();
        } catch (Exception ex) {
            NotificationUtil.error(ex.getMessage());
        }
    }
}
