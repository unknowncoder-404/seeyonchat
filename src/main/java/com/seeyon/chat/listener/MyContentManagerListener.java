package com.seeyon.chat.listener;

import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.settings.AppSettingsState;
import com.seeyon.chat.ui.ChatHistoryPanel;
import com.seeyon.chat.utils.ChatHttpUtil;
import com.seeyon.chat.utils.ChatSettingsUtil;
import com.seeyon.chat.utils.NotificationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Shaozz
 */
public class MyContentManagerListener implements ContentManagerListener {

    /**
     * 因为ToolWindow的内容切换时，selectionChanged()会被执行两次，所以需要一个状态变量用来排除掉第一次调用
     */
    private final AtomicBoolean state = new AtomicBoolean();

    private final ChatHistoryPanel chatHistoryPanel;

    public MyContentManagerListener(ChatHistoryPanel chatHistoryPanel) {
        this.chatHistoryPanel = chatHistoryPanel;
    }

    @Override
    public void selectionChanged(@NotNull ContentManagerEvent event) {
        if (state.compareAndSet(false, true)) {
            return;
        }
        state.set(false);

        if (event.getIndex() == 1) {
            if (!ChatSettingsUtil.checkSettings()) {
                return;
            }
            try {
                List<Chat> chats = ChatHttpUtil.getChats(AppSettingsState.getInstance().getChatbotId());
                chatHistoryPanel.load(chats);
            } catch (Exception e) {
                NotificationUtil.error(e.getMessage());
            }
        }
    }
}
