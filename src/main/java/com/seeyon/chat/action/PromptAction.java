package com.seeyon.chat.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.toolWindow.ChatToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Shaozz
 */
public class PromptAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        ChatToolWindowFactory.showToolWindow(project);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        assert editor != null;
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            Document document = editor.getDocument();
            selectedText = document.getText();
        }

        String prompt = e.getPresentation().getText();

        String questionContent = prompt + "\n```\n" + selectedText + "\n```";

        ChatService.getInstance(project).sendMessage(questionContent);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!ChatService.getInstance(e.getProject()).isLocked());
    }
}
