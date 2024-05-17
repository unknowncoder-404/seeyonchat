package com.seeyon.chat.listener;

import com.seeyon.chat.toolWindow.ChatToolWindowService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Shaozz
 */
public class StopActonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatToolWindowService service = ChatToolWindowService.getInstance();
        if (service.getFuture() != null) {
            service.getFuture().cancel(true);
        }
        SwingUtilities.invokeLater(() -> service.getChatToolWindow().aroundSend(true));
        service.unlock();
    }
}
