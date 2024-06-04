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
import java.util.List;

/**
 * @author Shaozz
 */
public class ChatPanel extends JBScrollPane {

    private final JPanel contentPanel;

    private final MyAdjustmentListener scrollListener = new MyAdjustmentListener();

    private final RoundRectPanel loaderPanel;

    public ChatPanel() {
        contentPanel = new JPanel(new VerticalLayout(JBUI.scale(10)));
        contentPanel.setBorder(JBUI.Borders.empty(10));

        setViewportView(contentPanel);
        setBorder(JBUI.Borders.empty());
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setAutoscrolls(true);

        // Add loaderLabel
        loaderPanel = new RoundRectPanel(new BorderLayout());
        loaderPanel.setBorder(JBUI.Borders.empty(10));
        loaderPanel.setBackground(ChatColor.ANSWER_BG_COLOR);
        JLabel label = new JLabel(ChatBundle.message("ui.chat.loader"), new AnimatedIcon.Default(), SwingConstants.LEFT);
        loaderPanel.add(label, BorderLayout.WEST);
    }

    public void addChat(ChatCell cell) {
        contentPanel.add(cell);

        contentPanel.revalidate();// 重新计算布局
        contentPanel.repaint();// 刷新界面
    }

    public void removeAllChats() {
        contentPanel.removeAll();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void replaceAllChats(List<ChatCell> chatCells) {
        contentPanel.removeAll();
        chatCells.forEach(contentPanel::add);

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
        getVerticalScrollBar().
                addAdjustmentListener(scrollListener);
    }

    public void removeScrollListener() {
        getVerticalScrollBar().
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
