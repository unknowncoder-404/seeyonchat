package com.seeyon.chat.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

public class PsiFileRetrieverUtil  {

    /**
     * 获取指定类的PsiFile对象
     *
     * @param project 当前项目
     * @param className 完整类名，例如 "com.seeyon.boot.domain.entity.BaseEntity"
     * @return 查找到的PsiFile对象，找不到则返回null
     */
    public static PsiFile getPsiFile(Project project, String className) {
        PsiClass psiClass = findClass(project, className);
        if (psiClass != null) {
            return psiClass.getContainingFile();
        }
        return null;
    }

    /**
     * 查找指定类的PsiClass对象
     *
     * @param project 当前项目
     * @param className 完整类名，例如 "com.seeyon.boot.domain.entity.BaseEntity"
     * @return 查找到的PsiClass对象，找不到则返回null
     */
    public static PsiClass findClass(Project project, String className) {
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        String shortName = extractClassName(className);
        PsiClass[] classes = cache.getClassesByName(shortName, GlobalSearchScope.allScope(project));

        for (PsiClass psiClass : classes) {
            if (className.equals(psiClass.getQualifiedName())) {
                return psiClass;
            }
        }

        return null;
    }

    /**
     * 从全路径信息中提取类名
     *
     * @param fullClassName 全路径信息，例如 "com.seeyon.boot.domain.entity.BaseEntity"
     * @return 类名，例如 "BaseEntity"
     */
    public static String extractClassName(String fullClassName) {
        if (fullClassName == null || fullClassName.isEmpty()) {
            throw new IllegalArgumentException("全路径信息不能为空");
        }

        int lastDotIndex = fullClassName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fullClassName;
        }

        return fullClassName.substring(lastDotIndex + 1);
    }


}