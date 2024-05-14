package com.seeyon.chat.listener;

import com.seeyon.chat.toolWindow.ChatToolWindowService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Shaozz
 */
public class SendActionListener extends KeyAdapter implements ActionListener {

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isControlDown() && !e.isShiftDown()) {
            e.consume();
            ChatToolWindowService.getInstance().getChatToolWindow().getSearchBoxComponent().getSendButton().doClick();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String data = ChatToolWindowService.getInstance().getChatToolWindow().getSearchBoxComponent().getTextArea().getText();
        ChatToolWindowService.getInstance().actionPerformed(data);
    }

}
