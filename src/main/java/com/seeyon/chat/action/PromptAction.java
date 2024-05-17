package com.seeyon.chat.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.seeyon.chat.toolWindow.ChatToolWindowService;
import com.seeyon.chat.utils.ChatBundle;
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
        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow(ChatBundle.message("plugin.name"));
        if (!toolWindow.isActive()) {
            toolWindow.activate(null);
        }
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        assert editor != null;
        String selectedText = editor.getSelectionModel().getSelectedText();

        String prompt = e.getPresentation().getText();

        String questionContent = "<p>" +prompt + "</p>\n" + "<pre><code>" + selectedText + "</code></pre>";

        ChatToolWindowService.getInstance().actionPerformed(questionContent);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!ChatToolWindowService.getInstance().isLocked());
    }
}
