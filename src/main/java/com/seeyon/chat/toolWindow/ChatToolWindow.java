package com.seeyon.chat.toolWindow;

import com.intellij.ui.OnePixelSplitter;
import com.seeyon.chat.ui.ChatBoxComponent;
import com.seeyon.chat.ui.SearchBoxComponent;

import javax.swing.*;
import java.awt.*;


/**
 * @author Shaozz
 */
public class ChatToolWindow {

    private final OnePixelSplitter splitter = new OnePixelSplitter(true, 0.95f);

    private final ChatBoxComponent chatBoxComponent = new ChatBoxComponent();

    private final SearchBoxComponent searchBoxComponent = new SearchBoxComponent();

    public ChatToolWindow() {
        splitter.setFirstComponent(chatBoxComponent.getComponent());
        splitter.setSecondComponent(searchBoxComponent.getComponent());
    }

    public JComponent getContentPanel() {
        return splitter;
    }

    public ChatBoxComponent getChatBoxComponent() {
        return chatBoxComponent;
    }

    public SearchBoxComponent getSearchBoxComponent() {
        return searchBoxComponent;
    }

    public void aroundSend(boolean actionState) {
        if (actionState) {
            // stop action
            searchBoxComponent.getComponent().remove(searchBoxComponent.getStopButton());
            searchBoxComponent.getComponent().add(searchBoxComponent.getSendButton(), BorderLayout.EAST);

            chatBoxComponent.removeScrollListener();
            chatBoxComponent.removeLoader();
        } else {
            // send action
            searchBoxComponent.getComponent().add(searchBoxComponent.getStopButton(), BorderLayout.EAST);
            searchBoxComponent.getComponent().remove(searchBoxComponent.getSendButton());
            searchBoxComponent.getTextArea().setText("");

            chatBoxComponent.addScrollListener();
            chatBoxComponent.addLoader();
        }
        searchBoxComponent.getTextArea().setEnabled(actionState);

        searchBoxComponent.getComponent().revalidate();// 重新计算布局
        searchBoxComponent.getComponent().repaint();// 刷新界面
    }

}
