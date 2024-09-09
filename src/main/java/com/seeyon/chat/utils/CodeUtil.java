package com.seeyon.chat.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiAnnotation;

public class CodeUtil {

    public static void extractClassInfo(Project project, PsiFile psiFile, String className) {
        if (!(psiFile instanceof PsiJavaFile)) {
            System.out.println("Not a Java file.");
            return;
        }

        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        PsiClass[] classes = javaFile.getClasses();

        for (PsiClass psiClass : classes) {
            if (psiClass.getName().equals(className)) {
                printClassInfo(psiClass);
                return;
            }
        }

        System.out.println("Class " + className + " not found.");
    }

    private static void printClassInfo(PsiClass psiClass) {
        // Print class definition
        System.out.println("Class: " + psiClass.getName());

        // Print class description from comments
        PsiElement prevSibling = psiClass.getPrevSibling();
        while (prevSibling != null) {
            if (prevSibling instanceof PsiComment) {
                System.out.println("Description: " + ((PsiComment) prevSibling).getText());
                break;
            }
            prevSibling = prevSibling.getPrevSibling();
        }

        // Print methods
        for (PsiMethod method : psiClass.getMethods()) {
            System.out.println("Method: " + method.getName());
            printMethodAnnotations(method);
        }
    }

    private static void printMethodAnnotations(PsiModifierListOwner element) {
        PsiAnnotation[] annotations = element.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            System.out.println("Annotation: " + annotation.getQualifiedName());
        }
    }

}