package com.seeyon.chat.ui;

import com.intellij.util.ui.HtmlPanel;
import com.intellij.util.ui.JBUI;
import com.seeyon.chat.ui.color.ChatColor;
import com.seeyon.chat.ui.markdown.MarkdownComponent;
import com.seeyon.chat.ui.markdown.MarkdownStreamComponent;
import com.seeyon.chat.utils.MarkdownUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author Shaozz
 */
public class ChatCell extends RoundRectPanel {

    private MarkdownStreamComponent markdownStreamComponent;

    private ChatCell(boolean isSelf) {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(10));
        if (isSelf) {
            setBackground(ChatColor.NORMAL_BG_COLOR);
        } else {
            setBackground(ChatColor.ANSWER_BG_COLOR);
        }
    }

    public static ChatCell ofAsk(String text) {
        ChatCell chatCell = new ChatCell(true);
        HtmlPanel htmlPanel = new HtmlPanel() {
            @Override
            protected @NotNull @Nls String getBody() {
                return "";
            }
        };
        htmlPanel.setText(MarkdownUtil.markdownToHtml(text));
        chatCell.add(htmlPanel, BorderLayout.CENTER);
        return chatCell;
    }

    public static ChatCell ofAnswer(String text) {
        ChatCell chatCell = new ChatCell(false);
        MarkdownComponent markdownComponent = new MarkdownComponent();
        markdownComponent.updateContent(text);
        chatCell.add(markdownComponent.getComponent(), BorderLayout.CENTER);
        return chatCell;
    }

    public static ChatCell ofAnswer() {
        ChatCell chatCell = new ChatCell(false);
        chatCell.markdownStreamComponent = new MarkdownStreamComponent();
        chatCell.add(chatCell.markdownStreamComponent.getComponent(), BorderLayout.CENTER);
        return chatCell;
    }

    public void append(String text) {
        markdownStreamComponent.append(text);
    }

}
