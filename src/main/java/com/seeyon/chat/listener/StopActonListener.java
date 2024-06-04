package com.seeyon.chat.listener;

import com.seeyon.chat.core.service.ChatService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Shaozz
 */
public class StopActonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatService.getInstance().stopGenerating();
    }
}
