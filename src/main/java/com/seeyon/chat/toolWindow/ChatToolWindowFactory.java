package com.seeyon.chat.toolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.listener.MyContentManagerListener;
import com.seeyon.chat.toolWindow.action.NewChatAction;
import com.seeyon.chat.toolWindow.action.SettingsAction;
import com.seeyon.chat.ui.ChatHistoryPanel;
import com.seeyon.chat.utils.ChatBundle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Shaozz
 */
public class ChatToolWindowFactory implements ToolWindowFactory, DumbAware {

    private static final String TOOL_WINDOW_ID = "致慧泉";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();

        ChatToolWindowContent chatToolWindowContent = ChatService.getInstance(project).getToolWindowContent();
        Content chatContent = ContentFactory.getInstance().createContent(chatToolWindowContent.getContentPanel(), "问答", false);
        contentManager.addContent(chatContent);

        ChatHistoryPanel chatHistoryPanel = new ChatHistoryPanel(project);
        Content chatHistoryContent = ContentFactory.getInstance().createContent(chatHistoryPanel, "历史", false);
        contentManager.addContent(chatHistoryContent);

        // settings
        SettingsAction settingsAction = new SettingsAction(ChatBundle.message("action.settings.text"), AllIcons.General.Settings);
        // new chat
        NewChatAction newChatAction = new NewChatAction(ChatBundle.message("action.newChat.text"), AllIcons.General.Add, contentManager, chatContent);
        toolWindow.setTitleActions(List.of(settingsAction, newChatAction));

        toolWindow.addContentManagerListener(new MyContentManagerListener(chatHistoryPanel));
    }

    public static ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID);
    }

    public static void showToolWindow(Project project) {
        ToolWindow toolWindow = getToolWindow(project);
        if (!toolWindow.isVisible()) {
            toolWindow.show(null);
        }
    }

}
