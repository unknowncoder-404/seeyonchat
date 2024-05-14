package com.seeyon.chat.toolWindow;

import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;

/**
 * @author Shaozz
 */
public class ChatPanel extends JBPanel<ChatPanel> {

    private final MarkdownPanel markdownPanel = new MarkdownPanel();

    private JLabel loaderLabel;

    public ChatPanel(String content, boolean isSelf) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBorder(JBUI.Borders.empty(10));

        if (isSelf) {
            setBackground(new JBColor(0xEAEEF7, 0x45494A));
            markdownPanel.append(content);
        } else {
            setBackground(new JBColor(0xE0EEF7, 0x2d2f30));
            // add loaderLabel
            loaderLabel = new JLabel(ChatBundle.message("ui.chat.loader"), new AnimatedIcon.Default(), SwingConstants.LEFT);
            add(loaderLabel, BorderLayout.WEST);
        }
        add(markdownPanel, BorderLayout.CENTER);
    }

    public void append(String str) {
        markdownPanel.append(str);
    }

    public void removeLoader() {
        if (loaderLabel == null) {
            return;
        }
        remove(loaderLabel);
        loaderLabel = null;

        revalidate();// 重新计算布局
        repaint();// 刷新界面
    }
}
