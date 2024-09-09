package com.seeyon.chat.utils;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;
import com.github.therapi.runtimejavadoc.FieldJavadoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {

    public static String getClassInfoAsJson(String className) {
        try {
            // 加载类
            Class<?> clazz = Class.forName(className);

            Map<String, Object> classInfo = new HashMap<>();

            // 获取类的定义信息
            classInfo.put("className", clazz.getName());
            classInfo.put("modifiers", Modifier.toString(clazz.getModifiers()));
            classInfo.put("superclass", clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "None");
            classInfo.put("interfaces", clazz.getInterfaces());

            // 获取类的注释信息
            ClassJavadoc classJavadoc = RuntimeJavadoc.getJavadoc(clazz);
            classInfo.put("classComment", classJavadoc.getComment().toString());

            // 获取 public 方法定义信息和注释信息，包括静态方法
            Map<String, Map<String, String>> methodsInfo = new HashMap<>();
            for (Method method : clazz.getMethods()) {
                Map<String, String> methodDetails = new HashMap<>();
                methodDetails.put("modifiers", Modifier.toString(method.getModifiers()));
                methodDetails.put("returnType", method.getReturnType().getName());
                MethodJavadoc methodJavadoc = RuntimeJavadoc.getJavadoc(method);
                methodDetails.put("comment", methodJavadoc.getComment().toString());
                methodsInfo.put(method.getName(), methodDetails);
            }
            classInfo.put("methods", methodsInfo);

            // 获取 public 属性定义信息和注释信息
            Map<String, Map<String, String>> fieldsInfo = new HashMap<>();
            for (Field field : clazz.getFields()) {
                Map<String, String> fieldDetails = new HashMap<>();
                fieldDetails.put("modifiers", Modifier.toString(field.getModifiers()));
                fieldDetails.put("type", field.getType().getName());
                FieldJavadoc fieldJavadoc = RuntimeJavadoc.getJavadoc(field);
                fieldDetails.put("comment", fieldJavadoc.getComment().toString());
                fieldsInfo.put(field.getName(), fieldDetails);
            }
            classInfo.put("fields", fieldsInfo);

            // 将结果转换为 JSON 格式
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(classInfo);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "{}";
        }
    }

//    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java -jar <jarfile> <fully-qualified-class-name>");
//            return;
//        }
//
//        String className = args[0];
//        // 获取类的信息并输出为 JSON
//        String classInfoJson = getClassInfoAsJson(className);
//        System.out.println(classInfoJson);
//    }
}