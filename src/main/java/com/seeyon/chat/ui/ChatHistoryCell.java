package com.seeyon.chat.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.util.text.Strings;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.seeyon.chat.core.model.Chat;
import com.seeyon.chat.core.service.ChatService;
import com.seeyon.chat.listener.RecoverChatListener;
import com.seeyon.chat.ui.color.ChatColor;
import com.seeyon.chat.utils.ChatHttpUtil;
import com.seeyon.chat.utils.NotificationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Shaozz
 */
public class ChatHistoryCell extends RoundRectPanel {

    private static final Font FONT = new Font("Monospaced", Font.PLAIN, 11);

    private final JLabel titleLabel;

    private final JLabel modifiedLabel;

    private final JLabel deleteLabel;

    private final String chatId;

    public ChatHistoryCell(ChatHistoryPanel historiesPanel, Chat chat, Project project) {
        this.chatId = chat.getId();

        this.setLayout(new BorderLayout());
        this.setBorder(JBUI.Borders.empty(10));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setBackground(ChatColor.ANSWER_BG_COLOR);

        RecoverChatListener mouseListener = new RecoverChatListener(chatId, project);
        this.addMouseListener(mouseListener);

        titleLabel = new JLabel(keepLength(chat.getTitle(), 50));
        titleLabel.setToolTipText(wrapTooltip(chat.getTitle()));
        titleLabel.setVerticalAlignment(1);
        titleLabel.setForeground(UIUtil.getTextAreaForeground());
        titleLabel.addMouseListener(mouseListener);

        modifiedLabel = new JLabel(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(chat.getUpdatedAt()));
        modifiedLabel.setForeground(JBColor.GRAY);
        modifiedLabel.setFont(FONT);

        deleteLabel = new JLabel(AllIcons.Actions.DeleteTag);
        deleteLabel.setVerticalAlignment(1);
        deleteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteLabel.setIcon(AllIcons.Actions.DeleteTagHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deleteLabel.setIcon(AllIcons.Actions.DeleteTag);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ChatHttpUtil.deleteChat(chatId);
                    historiesPanel.removeChat(ChatHistoryCell.this);

                    // 如果删除的是当前对话，则新建对话
                    ChatService chatService = ChatService.getInstance(project);
                    if (chatId.equals(chatService.getChatId())) {
                        chatService.createNewChat();
                    }
                } catch (Exception ex) {
                    NotificationUtil.error(ex.getMessage());
                }
            }
        });

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(deleteLabel, BorderLayout.EAST);
        this.add(headerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.add(modifiedLabel, BorderLayout.WEST);
        if (chatId.equals(ChatService.getInstance(project).getChatId())){
            footerPanel.add(new JLabel(AllIcons.General.InspectionsEye), BorderLayout.EAST);
        }
        this.add(footerPanel, BorderLayout.SOUTH);
    }

    private String wrapTooltip(String str) {
        if (Strings.isEmpty(str)) {
            return str;
        }
        if (str.length() > 512) {
            str = str.substring(0, 512) + "\n...";
        }
        str = StringUtil.escapeXmlEntities(str);
        str = str.replace(" ", "&nbsp;");
        return str;
    }

    private static String keepLength(String str, int maxLength) {
        if (Strings.isEmpty(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            float length = 0.0F;

            for (int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);
                length += isChineseChar(c) ? 2.0F : 1.0F;

                if (length > maxLength) {
                    break;
                }

                sb.append(c);
            }

            String result = sb.toString().trim();
            if (sb.length() < str.length()) {
                result = result + "...";
            }

            return result;
        }
    }

    private static boolean isChineseChar(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        } else {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS || ub == Character.UnicodeBlock.VERTICAL_FORMS;
        }
    }
}
