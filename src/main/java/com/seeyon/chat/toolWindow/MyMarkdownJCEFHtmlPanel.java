//package com.seeyon.chat.toolWindow;
//
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.project.ProjectManager;
//import com.intellij.testFramework.LightVirtualFile;
//import com.intellij.util.ui.UIUtil;
//import org.intellij.plugins.markdown.ui.preview.html.MarkdownUtil;
//import org.intellij.plugins.markdown.ui.preview.jcef.MarkdownJCEFHtmlPanel;
//
//import javax.swing.*;
//
///**
// * @author Shaozz
// */
//public class MyMarkdownJCEFHtmlPanel {
//
//    private LightVirtualFile file = new LightVirtualFile();
//    private MarkdownJCEFHtmlPanel panel = new MarkdownJCEFHtmlPanel();
//    private Project project = ProjectManager.getInstance().getDefaultProject();
//
//    private StringBuilder sb = new StringBuilder();
//
//    public void append(String content) {
//        sb.append(content);
//
//        String html = UIUtil.invokeAndWaitIfNeeded(() -> MarkdownUtil.INSTANCE.generateMarkdownHtml(file, sb.toString(), project));
//        panel.setHtml(html, 0);
//    }
//
//    public JComponent getComponent() {
//        return panel.getComponent();
//    }
//}
