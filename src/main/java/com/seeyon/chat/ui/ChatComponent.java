package com.seeyon.chat.ui;

import com.intellij.util.ui.HtmlPanel;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.ui.color.ChatColor;
import com.seeyon.chat.ui.markdown.MarkdownStreamComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author Shaozz
 */
public class ChatComponent {

    private final JPanel mainPanel;

    private MarkdownStreamComponent markdownStreamComponent;

    private ChatComponent(boolean isSelf) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBorder(JBUI.Borders.empty(10));
        if (isSelf) {
            mainPanel.setBackground(ChatColor.NORMAL_BG_COLOR);
        } else {
            mainPanel.setBackground(ChatColor.ANSWER_BG_COLOR);
        }
    }

    public static ChatComponent ofQuestion(String text) {
        ChatComponent chatComponent = new ChatComponent(true);
        HtmlPanel htmlPanel = new HtmlPanel() {
            @Override
            protected @NotNull @Nls String getBody() {
                return "";
            }
        };
        htmlPanel.setText(text);
        chatComponent.mainPanel.add(htmlPanel, BorderLayout.CENTER);
        return chatComponent;
    }

    public static ChatComponent ofAnswer() {
        ChatComponent chatComponent = new ChatComponent(false);
        chatComponent.markdownStreamComponent = new MarkdownStreamComponent();
        chatComponent.mainPanel.add(chatComponent.markdownStreamComponent.getComponent(), BorderLayout.CENTER);
        return chatComponent;
    }

    public void append(String text) {
        markdownStreamComponent.append(text);
    }

    public JComponent getComponent() {
        return mainPanel;
    }

}
