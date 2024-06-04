package com.seeyon.chat.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
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
        ChatToolWindowFactory.showToolWindow(e.getProject());
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        assert editor != null;
        String selectedText = editor.getSelectionModel().getSelectedText();

        String prompt = e.getPresentation().getText();

        String questionContent = "<p>" +prompt + "</p>\n" + "<pre><code>" + selectedText + "</code></pre>";

        ChatService.getInstance().actionPerformed(questionContent);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!ChatService.getInstance().isLocked());
    }
}
