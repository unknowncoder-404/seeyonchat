package com.seeyon.chat.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.utils.ChatBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 搜索框
 *
 * @author Shaozz
 */
public class AskInputPanel extends JPanel {

    private final RoundRectPanel mainPanel;

    private final JBTextArea textArea;

    private final JLabel sendLabel;

    private final JLabel stopLabel;

    public AskInputPanel(Project project) {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(10));

        mainPanel = new RoundRectPanel(new BorderLayout());
        mainPanel.setBorderColor(UIUtil.getFocusedBorderColor(), 1);
        mainPanel.setBorder(JBUI.Borders.empty(10));
        add(mainPanel, BorderLayout. CENTER);

        textArea = new JBTextArea();
        textArea.setLineWrap(true);// 自动换行
        textArea.setOpaque(false);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isControlDown() && !e.isShiftDown()) {
                    ChatService.getInstance(project).sendMessage();
                }
            }
        });
        mainPanel.add(textArea, BorderLayout.CENTER);

        sendLabel = new JLabel(AllIcons.Actions.Forward);
        sendLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChatService.getInstance(project).sendMessage();
            }
        });
        mainPanel.add(sendLabel, BorderLayout.EAST);

        stopLabel = new JLabel(ChatBundle.message("ui.button.stop"), AllIcons.Actions.Suspend, SwingConstants.LEFT);
        stopLabel.setOpaque(false);
        stopLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        stopLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChatService.getInstance(project).stopGenerating();
            }
        });
    }

    public JBTextArea getTextArea() {
        return textArea;
    }

    public void addSendLabel() {
        mainPanel.add(sendLabel, BorderLayout.EAST);
    }

    public void removeSendLabel() {
        mainPanel.remove(sendLabel);
    }

    public void addStopLabel() {
        mainPanel.add(stopLabel, BorderLayout.EAST);
    }

    public void removeStopLabel() {
        mainPanel.remove(stopLabel);
    }

}
