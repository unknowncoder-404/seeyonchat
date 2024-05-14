package com.seeyon.chat.toolWindow;

import com.intellij.ui.OnePixelSplitter;

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
        splitter.setFirstComponent(chatBoxComponent);
        splitter.setSecondComponent(searchBoxComponent);
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

    public void aroundSend(boolean hasBeen) {
        if (hasBeen) {
            searchBoxComponent.remove(searchBoxComponent.getStopButton());
            searchBoxComponent.add(searchBoxComponent.getSendButton(), BorderLayout.EAST);

            chatBoxComponent.removeScrollListener();
        } else {
            searchBoxComponent.add(searchBoxComponent.getStopButton(), BorderLayout.EAST);
            searchBoxComponent.remove(searchBoxComponent.getSendButton());
            searchBoxComponent.getTextArea().setText("");

            chatBoxComponent.addScrollListener();
        }
        searchBoxComponent.getTextArea().setEnabled(hasBeen);

        searchBoxComponent.revalidate();// 重新计算布局
        searchBoxComponent.repaint();// 刷新界面
    }

}
