package com.seeyon.chat.action;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.seeyon.chat.utils.FileUtil;
import org.jetbrains.annotations.NotNull;
import com.seeyon.chat.task.TestCaseTask;

public class GenerateUnitTestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前项目
        Project project = e.getProject();
        if (project == null) {
            return;
        }

//        // 获取当前选中的文件
//        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
//        if (!(psiFile instanceof PsiJavaFile)) {
//            Messages.showMessageDialog(project, "请选择一个Java文件", "错误", Messages.getErrorIcon());
//            return;
//        }
//
//        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
//        PsiClass[] classes = javaFile.getClasses();
//        if (classes.length == 0) {
//            Messages.showMessageDialog(project, "Java文件中没有类", "错误", Messages.getErrorIcon());
//            return;
//        }
//
//        // 获取当前文件
//        if (psiFile == null || !(psiFile instanceof PsiJavaFile)) {
//            return;
//        }
        String filePath = FileUtil.getCurrFileTestCaseFolder(e);
        VirtualFile defaultFolder = LocalFileSystem.getInstance().findFileByPath(filePath);
        // 创建文件选择描述符
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle("选择单元测试生成的路径");
        descriptor.setDescription("请选择单元测试生成的路径（会覆盖内部同名文件）");

        // 显示文件选择对话框
        VirtualFile selectedFolder = FileChooser.chooseFile(descriptor, project, defaultFolder);
        String newFilePath = "";
        if (selectedFolder != null) {
            newFilePath = selectedFolder.getPath();
        }else{
            Messages.showInfoMessage(project, "目标文件路径不存在", "生成单元测试");
            return;
        }

        System.out.println(newFilePath);

        // 生成单元测试
        // 启动异步任务
        ProgressManager.getInstance().run(new TestCaseTask( e,newFilePath));
    }
    @Override
    public void update(@NotNull AnActionEvent e) {
        // 仅当选中文件是Java文件时才启用菜单项
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        e.getPresentation().setEnabledAndVisible(psiFile instanceof PsiJavaFile);
    }

}
