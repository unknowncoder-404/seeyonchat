package com.seeyon.chat.toolWindow;

import com.intellij.util.ui.HtmlPanel;
import com.intellij.util.ui.UIUtil;
import com.seeyon.chat.utils.MarkdownUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author Shaozz
 */
public class MarkdownPanel extends HtmlPanel {

    private final StringBuffer content = new StringBuffer();

    @Override
    protected @NotNull @Nls String getBody() {
        return content == null ? "" : content.toString();
    }

    @Override
    protected @NotNull Font getBodyFont() {
        return UIUtil.getLabelFont();
    }

    public void append(String str) {
        content.append(str);
//        Document doc = getDocument();
//        if (doc != null) {
//            try {
//                doc.insertString(doc.getLength(), str, null);
//            } catch (BadLocationException ignored) {
//            }
//        }

        String html = MarkdownUtil.markdownToHtml(content.toString());
        setBody(html);
        revalidate();
        repaint();
    }
}
