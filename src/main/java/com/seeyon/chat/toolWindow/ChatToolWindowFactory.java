package com.seeyon.chat.toolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.seeyon.chat.toolWindow.action.NewChatAction;
import com.seeyon.chat.toolWindow.action.SettingsAction;
import com.seeyon.chat.utils.ChatBundle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Shaozz
 */
public class ChatToolWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ChatToolWindow chatToolWindow = ChatToolWindowService.getInstance().getChatToolWindow();
        Content content = ContentFactory.getInstance().createContent(chatToolWindow.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);

        // settings
        SettingsAction settingsAction = new SettingsAction(ChatBundle.message("action.settings.text"), AllIcons.General.Settings);
        // new chat
        NewChatAction newChatAction = new NewChatAction(ChatBundle.message("action.newChat.text"), AllIcons.General.Add);

        toolWindow.setTitleActions(List.of(settingsAction, newChatAction));
    }

}
