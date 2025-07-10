package com.seeyon.chat.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.ui.color.ChatColor;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Shaozz
 */
public class ChatPanel extends JBScrollPane {

    private final JPanel contentPanel;

    private final MyAdjustmentListener scrollListener = new MyAdjustmentListener();

    private final RoundRectPanel loadingPanel;

    private final JPanel regeneratePanel;

    private ChatCell latestChatCell;

    public ChatPanel(Project project) {
        contentPanel = new JPanel(new VerticalLayout(JBUI.scale(10)));
        contentPanel.setBorder(JBUI.Borders.empty(10));

        setViewportView(contentPanel);
        setBorder(JBUI.Borders.empty());
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setAutoscrolls(true);

        // Add loading label
        JLabel loadingLabel = new JLabel(ChatBundle.message("ui.chat.loading"), new AnimatedIcon.Default(), SwingConstants.LEFT);
        loadingPanel = new RoundRectPanel(new BorderLayout());
        loadingPanel.setBorder(JBUI.Borders.empty(10));
        loadingPanel.setBackground(ChatColor.ANSWER_BG_COLOR);
        loadingPanel.add(loadingLabel, BorderLayout.WEST);

        // Add regenerate label
        JLabel regenerateLabel = new JLabel(ChatBundle.message("ui.chat.regenerate"), AllIcons.Actions.Refresh, SwingConstants.LEFT);
        regenerateLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regenerateLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ChatService.getInstance(project).regenerate();
                } catch (Exception ex) {
                }
            }
        });
        regeneratePanel = new JPanel(new BorderLayout());
        regeneratePanel.setOpaque(false);
        regeneratePanel.add(regenerateLabel, BorderLayout.EAST);
    }

    public void addChatCell(ChatCell cell) {
        contentPanel.add(cell);
        latestChatCell = cell;
    }

    public ChatCell getLatestChatCell() {
        return latestChatCell;
    }

    public void removeLatestChatCell() {
        contentPanel.remove(latestChatCell);
        latestChatCell = null;
    }

    public void removeChat() {
        contentPanel.removeAll();
        latestChatCell = null;

        repaintContent();
    }

    public void replaceChat(List<ChatCell> chatCells) {
        contentPanel.removeAll();
        if (!chatCells.isEmpty()) {
            chatCells.forEach(this::addChatCell);
            addRegenerateLabel();
        }
        repaintContent();
    }

    public void repaintContent() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void addLoadingLabel() {
        contentPanel.add(loadingPanel);
    }

    public void removeLoadingLabel() {
        contentPanel.remove(loadingPanel);
    }

    public void addRegenerateLabel() {
        contentPanel.add(regeneratePanel);
    }

    public void removeRegenerateLabel() {
        contentPanel.remove(regeneratePanel);
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
