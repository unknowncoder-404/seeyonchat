package com.seeyon.chat.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

/**
 * @author Shaozz
 */
public class MarkdownUtil {

    private static final Parser parser = Parser.builder().build();
    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    /**
     * 将Markdown文本转换为HTML。
     *
     * @param markdownText Markdown格式的文本
     * @return 转换后的HTML字符串
     */
    public static String markdownToHtml(String markdownText) {
        // 解析Markdown文本
        Node document = parser.parse(markdownText);
        // 将解析后的文档渲染为HTML
        return renderer.render(document);
    }

//    public static JComponent createPreviewComponent(String content) {
//        Project project = ProjectManager.getInstance().getDefaultProject();
//
//        LightVirtualFile file = new LightVirtualFile();
////        MarkdownJCEFHtmlPanel panel = new MarkdownJCEFHtmlPanel(project, file);
//        MarkdownJCEFHtmlPanel panel = new MarkdownJCEFHtmlPanel();
//
//        // Ensure the HTML generation and UI updates are run in the appropriate thread
//        String html = UIUtil.invokeAndWaitIfNeeded(() -> org.intellij.plugins.markdown.ui.preview.html.MarkdownUtil.INSTANCE.generateMarkdownHtml(file, content, project));
//
//        panel.setHtml(html, 0);
//        return panel.getComponent();
//    }

//    public static JComponent demo() {
//        InputStream resourceAsStream = MarkdownUtil.class.getResourceAsStream("/Demo.md");
//        try {
//            String markdownContent = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
//            return createPreviewComponent(markdownContent);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
