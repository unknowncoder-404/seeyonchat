package com.seeyon.chat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringLLMUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return isEmpty(str) == false;
    }

    public static String extractCodeBlock(String input) {
        // 定义正则表达式来匹配 ``` 或 ```java 代码块
        String regex = "```(?:java)?(.*?)```";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        // 如果找到匹配的代码块，返回第一个代码块的内容
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // 如果没有找到代码块，返回输入的字符串
        return input;
    }

    public static void main(String[] args) {
        // 示例输入
        String input = "这里是一些文本\n```\npublic class Test {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}\n```\n更多文本";

        // 提取代码块
        String result = extractCodeBlock(input);

        // 输出结果
        System.out.println(result);
    }
}
