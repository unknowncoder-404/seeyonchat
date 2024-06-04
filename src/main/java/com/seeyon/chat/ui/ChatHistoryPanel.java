package com.seeyon.chat.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Shaozz
 */
public class ChatHistoryPanel extends JPanel {

    private static final int MAX_COUNT = 50;

    private final Project project;

    private final JPanel contentPanel;

    private final JLabel mostCountLimitLabel;

    private boolean existMaxTips = false;


    public ChatHistoryPanel(Project project) {
        setLayout(new BorderLayout());
        this.project = project;

        contentPanel = new JPanel(new VerticalLayout(JBUI.scale(10)));
        contentPanel.setBorder(JBUI.Borders.empty(10));

        mostCountLimitLabel = new JLabel(ChatBundle.message("chat.history.label.most.recent", MAX_COUNT));

        JBScrollPane jbScrollPane = new JBScrollPane(contentPanel);
        jbScrollPane.setBorder(JBUI.Borders.empty());
        jbScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jbScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jbScrollPane.getVerticalScrollBar().setAutoscrolls(true);
        add(jbScrollPane, BorderLayout.CENTER);
    }

    public void load(List<Chat> chats) {
        contentPanel.removeAll();

        for (int i = 0; i < chats.size() && i < MAX_COUNT; i++) {
            contentPanel.add(new ChatHistoryCell(this, chats.get(i), project));
        }
        if (chats.size() >= MAX_COUNT) {
            contentPanel.add(mostCountLimitLabel);
            existMaxTips = true;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void removeChat(ChatHistoryCell cell) {
        contentPanel.remove(cell);

        if (existMaxTips) {
            contentPanel.remove(mostCountLimitLabel);
            existMaxTips = false;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

}
