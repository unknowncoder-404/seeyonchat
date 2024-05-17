package com.seeyon.chat.ui;

import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.ui.color.ChatColor;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author Shaozz
 */
public class ChatBoxComponent {

    private final JBScrollPane mainPanel;

    private final JPanel contentPanel;

    private final MyAdjustmentListener scrollListener = new MyAdjustmentListener();

    private JPanel loaderPanel;

    public ChatBoxComponent() {
        contentPanel = new JPanel(new VerticalLayout(JBUI.scale(10)));
        contentPanel.setBorder(JBUI.Borders.empty(10));

        mainPanel = new JBScrollPane(contentPanel);
        mainPanel.setBorder(JBUI.Borders.empty());
        mainPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.getVerticalScrollBar().setAutoscrolls(true);

        // Add loaderLabel
        loaderPanel = new JPanel(new BorderLayout());
        loaderPanel.setBorder(JBUI.Borders.empty(10));
        loaderPanel.setBackground(ChatColor.ANSWER_BG_COLOR);
        JLabel label = new JLabel(ChatBundle.message("ui.chat.loader"), new AnimatedIcon.Default(), SwingConstants.LEFT);
        loaderPanel.add(label, BorderLayout.WEST);
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public void addChat(JComponent component) {
        contentPanel.add(component);

        contentPanel.revalidate();// 重新计算布局
        contentPanel.repaint();// 刷新界面
    }

    public void removeAllChats() {
        contentPanel.removeAll();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void addLoader() {
        contentPanel.add(loaderPanel);
    }

    public void removeLoader() {
        contentPanel.remove(loaderPanel);
    }

    public void addScrollListener() {
        mainPanel.getVerticalScrollBar().
                addAdjustmentListener(scrollListener);
    }

    public void removeScrollListener() {
        mainPanel.getVerticalScrollBar().
                removeAdjustmentListener(scrollListener);
    }

    static class MyAdjustmentListener implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            // 滚动条停止滚动时，滚到最底部
            JScrollBar source = (JScrollBar) e.getSource();
            if (!source.getValueIsAdjusting()) {
                source.setValue(source.getMaximum());
            }
        }
    }
}
