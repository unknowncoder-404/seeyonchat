package com.seeyon.chat.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * @author Shaozz
 */
public class MarkdownUtil {

    private static final Parser parser;
    private static final HtmlRenderer renderer;

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

    static {
        MutableDataSet options = new MutableDataSet();
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        options.set(HtmlRenderer.ESCAPE_HTML, true);
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

}
