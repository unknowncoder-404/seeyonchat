package com.seeyon.chat.toolWindow;

import com.intellij.ui.OnePixelSplitter;
import com.seeyon.chat.ui.ChatPanel;
import com.seeyon.chat.ui.AskInputPanel;

import javax.swing.*;
import java.awt.*;


/**
 * @author Shaozz
 */
public class ChatToolWindowContent {

    private final OnePixelSplitter splitter;

    private final ChatPanel chatPanel;

    private final AskInputPanel askInputPanel;

    public ChatToolWindowContent() {
        chatPanel = new ChatPanel();
        askInputPanel = new AskInputPanel();
        splitter = new OnePixelSplitter(true, 0.95f);

        splitter.setFirstComponent(chatPanel);
        splitter.setSecondComponent(askInputPanel);
    }

    public JComponent getContentPanel() {
        return splitter;
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public AskInputPanel getSearchBoxComponent() {
        return askInputPanel;
    }

    public void aroundSend(boolean actionState) {
        if (actionState) {
            // after send
            askInputPanel.remove(askInputPanel.getStopButton());
            askInputPanel.add(askInputPanel.getSendButton(), BorderLayout.EAST);

            chatPanel.removeScrollListener();
            chatPanel.removeLoader();
        } else {
            // before send
            askInputPanel.add(askInputPanel.getStopButton(), BorderLayout.EAST);
            askInputPanel.remove(askInputPanel.getSendButton());
            askInputPanel.getTextArea().setText("");

            chatPanel.addScrollListener();
            chatPanel.addLoader();
        }
        askInputPanel.getTextArea().setEnabled(actionState);

        askInputPanel.revalidate();// 重新计算布局
        askInputPanel.repaint();// 刷新界面
    }

}
