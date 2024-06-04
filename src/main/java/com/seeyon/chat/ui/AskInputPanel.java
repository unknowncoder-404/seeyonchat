package com.seeyon.chat.ui;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.listener.SendActionListener;
import com.seeyon.chat.listener.StopActonListener;
import com.seeyon.chat.ui.color.ChatColor;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;


/**
 * 搜索框
 *
 * @author Shaozz
 */
public class AskInputPanel extends JPanel {

    private final JBTextArea textArea;

    private final JButton sendButton;

    private final JButton stopButton;

    public AskInputPanel() {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(10));// 添加外边距

        textArea = new JBTextArea();
        textArea.setLineWrap(true);// 自动换行
        textArea.setWrapStyleWord(true);// 断行不断字
        textArea.setBorder(JBUI.Borders.empty(10));
        textArea.setBackground(ChatColor.NORMAL_BG_COLOR);
        add(textArea, BorderLayout.CENTER);

        sendButton = new JButton(AllIcons.Actions.Forward);
        sendButton.setBorder(JBUI.Borders.empty());// 移除边框
        sendButton.setContentAreaFilled(false);// 去除按钮填充色，仅显示图标
        sendButton.setPreferredSize(new Dimension(40, 40));// 设置按钮大小
        sendButton.setOpaque(true);// 使按钮背景颜色生效
        sendButton.setBackground(textArea.getBackground());// 设置按钮背景颜色与textArea一致
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(sendButton, BorderLayout.EAST);

        stopButton = new JButton(ChatBundle.message("ui.button.stop"), AllIcons.Actions.Suspend);
        stopButton.setBorder(JBUI.Borders.empty());// 移除边框
        stopButton.setContentAreaFilled(false);// 去除按钮填充色，仅显示图标
        stopButton.setOpaque(true);// 使按钮背景颜色生效
        stopButton.setBackground(textArea.getBackground());// 设置按钮背景颜色与textArea一致
        stopButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // send listener
        SendActionListener sendActionListener = new SendActionListener();
        textArea.addKeyListener(sendActionListener);
        sendButton.addActionListener(sendActionListener);
        // stop listener
        stopButton.addActionListener(new StopActonListener());
    }

    public JBTextArea getTextArea() {
        return textArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

}
