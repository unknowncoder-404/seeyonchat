package com.seeyon.chat.ui.markdown;

import com.intellij.util.ui.HtmlPanel;
import com.seeyon.chat.utils.MarkdownUtil;

import java.util.regex.Matcher;

/**
 * @author Shaozz
 */
public class MarkdownStreamComponent extends MarkdownComponent {

    private HtmlPanel latestTextArea;

    private final StringBuilder sb = new StringBuilder();

    public MarkdownStreamComponent() {
        super();
    }

    public synchronized void append(String text) {
        sb.append(text);

        Matcher matcher = pattern.matcher(sb.toString());
        int lastEnd = 0;
        while (matcher.find()) {
            // Add the text before the code block
            String textBeforeCode = sb.substring(lastEnd, matcher.start());
            if (!textBeforeCode.isEmpty()) {
                String html = MarkdownUtil.markdownToHtml(textBeforeCode);
                if (latestTextArea == null) {
                    mainPanel.add(createTextArea(html));
                } else {
                    latestTextArea.setText(html);
                    latestTextArea = null;
                }
            } else if (latestTextArea != null) {
                mainPanel.remove(latestTextArea);
                latestTextArea = null;
            }

            // Add the code block with RSyntaxTextArea
            String code = matcher.group(2);
            String language = matcher.group(1);
            mainPanel.add(createCodeArea(code, language));

            lastEnd = matcher.end();
        }
        sb.delete(0, lastEnd);

        // Add the remaining text after the last code block
        if (!sb.isEmpty()) {
            String html = MarkdownUtil.markdownToHtml(sb.toString());
            if (!html.isEmpty()) {
                if (latestTextArea == null) {
                    latestTextArea = createTextArea(html);
                    mainPanel.add(latestTextArea);
                } else {
                    latestTextArea.setText(html);
                }
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

}
