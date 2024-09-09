package com.seeyon.chat.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JavaClassParser {

    public static void main(String[] args) {
        try {
            // 要解析的类名
            String className = "com.seeyon.demo.order.domain.service.OrderInfoService";

            // 加载类
            Class<?> clazz = Class.forName(className);

            // 获取包名
            String packageName = clazz.getPackage().getName();

            // 获取类名
            String simpleClassName = clazz.getSimpleName();

            // 获取类的所有导入包
            StringBuilder importPackages = new StringBuilder();
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                importPackages.append(field.getType().getName()).append(",");
            });
            Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
                importPackages.append(method.getReturnType().getName()).append(",");
                Arrays.stream(method.getParameterTypes()).forEach(param -> {
                    importPackages.append(param.getName()).append(",");
                });
            });

            // 获取类的所有方法
            Method[] methods = clazz.getDeclaredMethods();
            Map<String, Object> methodInfos = new HashMap<>();
            for (Method method : methods) {
                Map<String, Object> methodInfo = new HashMap<>();
                methodInfo.put("methodName", method.getName());
                methodInfo.put("methodParams", Arrays.toString(method.getParameterTypes()));
                methodInfo.put("methodContent", method.toString());
                methodInfo.put("rowCount", method.getParameterCount());
                methodInfo.put("ifConditionNum", countIfConditions(method));
                methodInfos.put(method.getName(), methodInfo);
            }

            // 生成输出
            Map<String, Object> output = new HashMap<>();
            output.put("classPackage", packageName);
            output.put("className", simpleClassName);
            output.put("classImport", importPackages.toString());
            output.put("classContent", clazz.toString());
            output.put("methodInfos", methodInfos);

            // 打印输出
            System.out.println(output);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int countIfConditions(Method method) {
        // 统计方法中的if条件数量
        String methodContent = method.toString();
        return methodContent.split("if \\(").length - 1;
    }
}