package com.seeyon.chat.utils;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.seeyon.chat.exception.BuzinessException;
import org.jetbrains.annotations.Nullable;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void save2TestFolder(String packagePath, String fileName, String code, AnActionEvent event, boolean isReplace) throws BuzinessException, IOException {
        // 目标目录
        File targetDir = null;
        if(StringLLMUtil.isEmpty(packagePath)) {
            throw new IOException("packagePath is null");
        } else {
            // 目标目录
            targetDir = new File(packagePath);
        }


        // 创建目标目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(targetDir+File.separator+fileName);
        if (targetFile.exists()) {
            if (isReplace){
                targetFile.delete();
            } else {
                throw new BuzinessException("文件已经存在"+targetFile.getAbsoluteFile());
            }
        }

        // 目标文件
        targetFile = new File(targetDir, fileName);

        // 将字符串代码写入到目标文件
        try (FileWriter writer = new FileWriter(targetFile)) {
            writer.write(code);
        } catch (IOException ex) {
            throw new BuzinessException("文件写入失败"+targetFile.getAbsoluteFile());
        }
    }

    public static String getCurrFileTestCaseFolder(AnActionEvent event)  {
        // 获取当前打开的文件
        VirtualFile currentFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (currentFile == null) {
            return "";
        }

        // 获取当前文件
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null || !(psiFile instanceof PsiJavaFile)) {
            return "";
        }
        // 获取当前文件所在的模块
        @Nullable Module module = ModuleUtil.findModuleForFile(currentFile, event.getProject());
        if (module == null) {
            return "";
        }
        String moduleRootPath = "";
        // 获取模块的根路径
        VirtualFile[] moduleRoots = ModuleRootManager.getInstance(module).getContentRoots();
        if (moduleRoots.length > 0) {
            // 假设第一个根路径为模块的根路径
            VirtualFile moduleRoot = moduleRoots[0];
            moduleRootPath = moduleRoot.getPath();
        }
        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        String packageName = javaFile.getPackageName();
        String packagePath = "/" + packageName.replace('.', '/') + "/";
        // 目标目录
        File targetDir = new File(moduleRootPath, "src/test/java/" + packagePath);
        return targetDir.getAbsolutePath();
    }

}
