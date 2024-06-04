package com.seeyon.chat.listener;

import com.seeyon.chat.core.service.ChatService;

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
            ChatService.getInstance().actionPerformed();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ChatService.getInstance().actionPerformed();
    }

}
