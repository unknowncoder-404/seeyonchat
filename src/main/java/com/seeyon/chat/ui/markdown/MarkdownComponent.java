package com.seeyon.chat.ui.markdown;

import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.HtmlPanel;
import com.seeyon.chat.ui.CodePanel;
import com.seeyon.chat.utils.MarkdownUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shaozz
 */
public class MarkdownComponent {

    protected static final Pattern pattern;

    protected final JPanel mainPanel;

    public MarkdownComponent() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(0));
        mainPanel.setOpaque(false);
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public void updateContent(String markdownContent) {
        // Set the HTML content to the text pane
        Matcher matcher = pattern.matcher(markdownContent);

        int lastEnd = 0;
        while (matcher.find()) {
            // Add the text before the code block
            String textBeforeCode = markdownContent.substring(lastEnd, matcher.start());
            if (!textBeforeCode.isEmpty()) {
                String html = MarkdownUtil.markdownToHtml(textBeforeCode);
                mainPanel.add(createTextArea(html));
            }

            // Add the code block with RSyntaxTextArea
            String code = matcher.group(2);
            String language = matcher.group(1);
            mainPanel.add(createCodeArea(code, language));

            lastEnd = matcher.end();
        }

        // Add the remaining text after the last code block
        String textAfterLastCode = markdownContent.substring(lastEnd);
        if (!textAfterLastCode.isEmpty()) {
            HtmlPanel textArea = createTextArea(MarkdownUtil.markdownToHtml(textAfterLastCode));
            mainPanel.add(textArea);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    protected static HtmlPanel createTextArea(String text) {
        HtmlPanel textPane = new HtmlPanel() {
            @Override
            protected @NotNull @Nls String getBody() {
                return "";
            }
        };
        textPane.setText(text);
        return textPane;
    }

    protected static CodePanel createCodeArea(String code, String language) {
        return new CodePanel(code, language);
    }

    static {
        pattern = Pattern.compile("```(\\w+)?\\n(.*?)\\n\\s*```", Pattern.DOTALL);
    }
}
