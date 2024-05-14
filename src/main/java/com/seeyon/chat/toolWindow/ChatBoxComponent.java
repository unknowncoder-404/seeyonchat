package com.seeyon.chat.toolWindow;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author Shaozz
 */
public class ChatBoxComponent extends JBScrollPane {

    private final JPanel contentPanel = new JPanel(new VerticalLayout(JBUI.scale(10)));
    private final MyAdjustmentListener scrollListener = new MyAdjustmentListener();

    /**
     * 记录当前最新的聊天面板
     */
    private ChatPanel currentChatPanel;

    public ChatBoxComponent() {
        setBorder(JBUI.Borders.empty(10, 10, 10, 0));
        setBackground(UIUtil.getListBackground());

        contentPanel.setOpaque(true);
        contentPanel.setBackground(UIUtil.getListBackground());
        contentPanel.setBorder(JBUI.Borders.emptyRight(10));

        setViewportView(contentPanel);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setAutoscrolls(true);
    }

    public void addChats(ChatPanel question, ChatPanel answer) {
        contentPanel.add(question);
        contentPanel.add(answer);

        revalidate();// 重新计算布局
        repaint();// 刷新界面

        currentChatPanel = answer;
    }

    public void removeLoader() {
        if (currentChatPanel != null) {
            currentChatPanel.removeLoader();
        }
    }

    public void removeAllChats() {
        contentPanel.removeAll();
        currentChatPanel = null;

        revalidate();
        repaint();
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
