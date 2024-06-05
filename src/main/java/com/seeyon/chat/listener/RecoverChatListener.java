package com.seeyon.chat.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.toolWindow.ChatToolWindowFactory;
import com.seeyon.chat.utils.NotificationUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Shaozz
 */
public class RecoverChatListener extends MouseAdapter {

    private final String chatId;

    private final Project project;

    public RecoverChatListener(String chatId, Project project) {
        this.chatId = chatId;
        this.project = project;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                ChatService chatService = ChatService.getInstance(project);
                if (!chatId.equals(chatService.getChatId())) {
                    chatService.recoverChat(chatId);
                }
                ChatToolWindowFactory.getToolWindow(project).getContentManager().selectPreviousContent();
            } catch (Exception ex) {
                NotificationUtil.error(ex.getMessage());
            }
        });
    }
}
