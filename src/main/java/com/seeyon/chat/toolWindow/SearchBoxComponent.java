package com.seeyon.chat.toolWindow;

import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.laf.darcula.ui.DarculaButtonUI;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.listener.SendActionListener;
import com.seeyon.chat.listener.StopActonListener;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;


/**
 * 搜索框
 *
 * @author Shaozz
 */
public class SearchBoxComponent extends JPanel {

    private final JBTextArea textArea = new JBTextArea();
    private final JButton sendButton = new JButton(AllIcons.Actions.Forward);
    private final JButton stopButton = new JButton(ChatBundle.message("ui.button.stop"), AllIcons.Actions.Suspend);
    private final SendActionListener sendActionListener = new SendActionListener();
    private final StopActonListener stopActonListener = new StopActonListener();

    public SearchBoxComponent() {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(10));// 添加外边距

        textArea.setLineWrap(true);// 自动换行
        textArea.setWrapStyleWord(true);// 断行不断字
        textArea.setBorder(JBUI.Borders.empty(10));
//        textArea.setFont(new Font("Arial", Font.PLAIN, 16)); // 设置字体样式
        // 监听Enter键事件
        textArea.addKeyListener(sendActionListener);
        add(textArea, BorderLayout.CENTER);

        sendButton.setBorder(JBUI.Borders.empty());// 移除边框
        sendButton.setContentAreaFilled(false);// 去除按钮填充色，仅显示图标
        sendButton.setPreferredSize(new Dimension(40, 40));// 设置按钮大小
        sendButton.setOpaque(true);// 使按钮背景颜色生效
        sendButton.setBackground(textArea.getBackground());// 设置按钮背景颜色与JTextArea一致
        sendButton.addActionListener(sendActionListener);
        add(sendButton, BorderLayout.EAST);

        stopButton.setUI(new DarculaButtonUI());
        stopButton.setBorder(JBUI.Borders.empty());// 移除边框
        stopButton.setContentAreaFilled(false);// 去除按钮填充色，仅显示图标
        stopButton.setOpaque(true);// 使按钮背景颜色生效
        stopButton.setBackground(textArea.getBackground());// 设置按钮背景颜色与JTextArea一致
        stopButton.addActionListener(stopActonListener);
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
