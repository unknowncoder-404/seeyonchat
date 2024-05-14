package com.seeyon.chat.toolWindow.action;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.seeyon.chat.settings.AppSettingsConfigurable;

import javax.swing.*;

/**
 * @author Shaozz
 */
public class SettingsAction extends DumbAwareAction {

    public SettingsAction(String text, Icon icon) {
        super(() -> text, icon);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), AppSettingsConfigurable.class);
    }
}
