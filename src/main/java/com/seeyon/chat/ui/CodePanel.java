package com.seeyon.chat.ui;

import com.intellij.icons.AllIcons;
import com.seeyon.chat.ui.markdown.MarkdownComponent;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @author Shaozz
 */
public class CodePanel extends JPanel {

    private static final Theme theme;

    private final RSyntaxTextArea codeArea;

    public CodePanel() {
        super(new BorderLayout());
        setOpaque(false);

        codeArea = new RSyntaxTextArea();
        codeArea.setEditable(false);
        codeArea.setLineWrap(true);
        codeArea.setWrapStyleWord(true);
        codeArea.setPopupMenu(null);// Remove the default right-click menu
        theme.apply(codeArea);// Apply the theme to the code area
        add(codeArea, BorderLayout.CENTER);
    }

    public CodePanel(String code, String language) {
        this();
        codeArea.setText(code);
        codeArea.setSyntaxEditingStyle(getSyntaxStyle(language));

        add(createHeaderPanel(code, language), BorderLayout.NORTH);
    }

    private JPanel createHeaderPanel(String code, String language) {
        // Create the header panel with language label and copy button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel languageLabel = new JLabel(language != null ? language : "text");
        JLabel copyLabel = new JLabel(AllIcons.Actions.Copy);
        copyLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringSelection stringSelection = new StringSelection(code);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }

        });
        headerPanel.add(languageLabel, BorderLayout.WEST);
        headerPanel.add(copyLabel, BorderLayout.EAST);
        return headerPanel;
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
            case "cpp":
                return SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
            case "csharp":
                return SyntaxConstants.SYNTAX_STYLE_CSHARP;
            case "clojure":
                return SyntaxConstants.SYNTAX_STYLE_CLOJURE;
            case "go":
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
        try {
            theme = Theme.load(MarkdownComponent.class.getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
