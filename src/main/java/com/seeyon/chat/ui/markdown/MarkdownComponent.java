package com.seeyon.chat.ui.markdown;

import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.HtmlPanel;
import com.seeyon.chat.utils.MarkdownUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shaozz
 */
public class MarkdownComponent {

    private static final Theme theme;

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
                HtmlPanel textArea = createTextArea(MarkdownUtil.markdownToHtml(textBeforeCode));
                mainPanel.add(textArea);
            }

            // Add the code block with RSyntaxTextArea
            String code = matcher.group(2);
            String language = matcher.group(1);
            RSyntaxTextArea codeArea = createCodeArea(code, language);
            mainPanel.add(codeArea);

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

    protected static RSyntaxTextArea createCodeArea(String code, String language) {
        RSyntaxTextArea codeArea = new RSyntaxTextArea();
        codeArea.setText(code);
        codeArea.setEditable(false);
        codeArea.setSyntaxEditingStyle(getSyntaxStyle(language));
        codeArea.setLineWrap(true);
        codeArea.setWrapStyleWord(true);
        codeArea.setPopupMenu(null);// Remove the default right-click menu

        theme.apply(codeArea);// Apply the theme to the code area
        return codeArea;
    }

    private static String getSyntaxStyle(String language) {
        if (language == null) {
            return SyntaxConstants.SYNTAX_STYLE_NONE;
        }
        switch (language.toLowerCase()) {
            case "java":
                return SyntaxConstants.SYNTAX_STYLE_JAVA;
            case "python":
                return SyntaxConstants.SYNTAX_STYLE_PYTHON;
            case "javascript":
                return SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
            case "html":
                return SyntaxConstants.SYNTAX_STYLE_HTML;
            case "css":
                return SyntaxConstants.SYNTAX_STYLE_CSS;
            case "c":
                return SyntaxConstants.SYNTAX_STYLE_C;
            case "clojure":
                return SyntaxConstants.SYNTAX_STYLE_CLOJURE;
            case "golang":
                return SyntaxConstants.SYNTAX_STYLE_GO;
            case "groovy":
                return SyntaxConstants.SYNTAX_STYLE_GROOVY;
            case "json":
                return SyntaxConstants.SYNTAX_STYLE_JSON;
            case "jsp":
                return SyntaxConstants.SYNTAX_STYLE_JSP;
            case "kotlin":
                return SyntaxConstants.SYNTAX_STYLE_KOTLIN;
            case "lua":
                return SyntaxConstants.SYNTAX_STYLE_LUA;
            case "php":
                return SyntaxConstants.SYNTAX_STYLE_PHP;
            case "ruby":
                return SyntaxConstants.SYNTAX_STYLE_RUBY;
            case "sql":
                return SyntaxConstants.SYNTAX_STYLE_SQL;
            case "typescript":
                return SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT;
            case "xml":
                return SyntaxConstants.SYNTAX_STYLE_XML;
            case "yaml":
                return SyntaxConstants.SYNTAX_STYLE_YAML;
            // Add more languages as needed
            default:
                return SyntaxConstants.SYNTAX_STYLE_NONE;
        }
    }

    static {
        pattern = Pattern.compile("```(\\w+)?\\n([\\s\\S]*?)\\n```");
        try {
            theme = Theme.load(MarkdownComponent.class.getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
