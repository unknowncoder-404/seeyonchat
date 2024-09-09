package com.seeyon.chat.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.HashMap;
import java.util.Map;

public class ClassInfoExtractor {

//    public static void main(String[] args) {
//        String className = "com.seeyon.chat.core.service.LLMCallService"; // 指定类的全路径
//        Project project = ProjectManager.getInstance().getOpenProjects()[0]; // 获取当前打开的项目
//
//        ApplicationManager.getApplication().runReadAction(() -> {
//            PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
//            if (psiClass != null) {
//                JsonObject classInfoJson = getClassInfo(psiClass);
//                Gson gson = new Gson();
//                String jsonOutput = gson.toJson(classInfoJson);
//                System.out.println(jsonOutput);
//            } else {
//                System.out.println("Class not found: " + className);
//            }
//        });
//    }

    public static String getClassInfoJSON(Project project,String className){
        return ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
            PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
            if (psiClass != null) {
                JsonObject classInfoJson = getClassInfo(psiClass);
                Gson gson = new Gson();
                String jsonOutput = gson.toJson(classInfoJson);
                return jsonOutput;
            } else {
                System.out.println("Class not found: " + className);
                return "";
            }
        });

    }

    private static JsonObject getClassInfo(PsiClass psiClass) {
        JsonObject classInfo = new JsonObject();
        classInfo.addProperty("className", psiClass.getQualifiedName());

        // 获取类注释
        PsiComment classComment = findCommentForElement(psiClass);
        classInfo.addProperty("classComment", classComment != null ? classComment.getText() : "");

        // 获取属性信息
        Map<String, String> fieldsInfo = new HashMap<>();
        for (PsiField field : psiClass.getAllFields()) {
            PsiComment fieldComment = findCommentForElement(field);
            fieldsInfo.put(field.getName(), fieldComment != null ? fieldComment.getText() : "");
        }
        classInfo.add("fields", new Gson().toJsonTree(fieldsInfo));

        // 获取方法信息
        Map<String, String> methodsInfo = new HashMap<>();
        for (PsiMethod method : psiClass.getAllMethods()) {
            PsiComment methodComment = findCommentForElement(method);
            methodsInfo.put(method.getName(), methodComment != null ? methodComment.getText() : "");
        }
        classInfo.add("methods", new Gson().toJsonTree(methodsInfo));

        return classInfo;
    }

    private static PsiComment findCommentForElement(PsiElement element) {
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null && !(prevSibling instanceof PsiComment)) {
            prevSibling = prevSibling.getPrevSibling();
        }
        return (PsiComment) prevSibling;
    }
}
