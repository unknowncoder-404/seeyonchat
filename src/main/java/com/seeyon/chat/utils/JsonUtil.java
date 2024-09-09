package com.seeyon.chat.utils;

import com.google.gson.*;
import com.intellij.openapi.ui.Messages;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {


    public static String getClassPackage(String jsonString) {
//        String jsonString = "{\n" +
//                "    \"classInfo\": \"#{替换method代码}\",\n" +
//                "    \"methodInfos\": [{\"methodInfo\": \"\"}]\n" +
//                "}";

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // 读取classInfo
        if(jsonObject.get("classPackage") != null){
            return jsonObject.get("classPackage").getAsString();
        }
        return "";

    }

    public static String getClassName(String jsonString) {
//        String jsonString = "{\n" +
//                "    \"classInfo\": \"#{替换method代码}\",\n" +
//                "    \"methodInfos\": [{\"methodInfo\": \"\"}]\n" +
//                "}";

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // 读取classInfo
        if(jsonObject.get("className") != null){
            return jsonObject.get("className").getAsString();
        }
        return "";

    }


    public static String getClassContent(String jsonString) {
//        String jsonString = "{\n" +
//                "    \"classInfo\": \"#{替换method代码}\",\n" +
//                "    \"methodInfos\": [{\"methodInfo\": \"\"}]\n" +
//                "}";

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // 读取classInfo
        if(jsonObject.get("classContent") != null){
            return jsonObject.get("classContent").getAsString();
        }
        return "";

    }
    public static String getClassImport(String jsonString) {

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // 读取classInfo
        if(jsonObject.get("classImport") != null){
            return jsonObject.get("classImport").getAsString();
        }
        return "";

    }


    public static List<String> getMethodNameList(String jsonString) {
//        String jsonString = "{\n" +
//                "    \"classInfo\": \"#{替换method代码}\",\n" +
//                "    \"methodInfos\": [{\"methodInfo\": \"\"}]\n" +
//                "}";

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        List<String> methodNameList = new ArrayList<>();

        // 读取methodInfos
        JsonArray methodInfos = jsonObject.getAsJsonArray("methodInfos");
        StringBuilder methodInfoBuilder = new StringBuilder();
        if(methodInfos != null) {
            for (int i = 0; i < methodInfos.size(); i++) {
                if(methodInfos.get(i) != null){
                    methodNameList.add( methodInfos.get(i).getAsJsonObject().get("methodName").getAsString());
                }

            }
        }

        return methodNameList;
    }

    public static List<String> getMethodContentList(String jsonString) {
//        String jsonString = "{\n" +
//                "    \"classInfo\": \"#{替换method代码}\",\n" +
//                "    \"methodInfos\": [{\"methodInfo\": \"\"}]\n" +
//                "}";

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        List<String> methodInfoList = new ArrayList<>();

        // 读取methodInfos
        JsonArray methodInfos = jsonObject.getAsJsonArray("methodInfos");
        StringBuilder methodInfoBuilder = new StringBuilder();
        if(methodInfos != null) {
            for (int i = 0; i < methodInfos.size(); i++) {
                if(methodInfos.get(i) != null){
                    methodInfoList.add( methodInfos.get(i).getAsJsonObject().get("methodContent").getAsString());
                }

            }
        }

        return methodInfoList;
    }

    public static String getAzureMessage(String jsonString) {


        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();


        // 读取methodInfos
        JsonArray methodInfos = jsonObject.getAsJsonArray("choices");
        StringBuilder methodInfoBuilder = new StringBuilder();
        if(methodInfos != null) {
            for (int i = 0; i < methodInfos.size(); i++) {
                if(methodInfos.get(i) != null){
                    JsonObject messageObj = methodInfos.get(i).getAsJsonObject().get("message").getAsJsonObject();
                    String role = messageObj.get("role").getAsString();
                    String content = messageObj.get("content").getAsString();
                    if("assistant".equals(role)){
                        return content;
                    }
                }

            }
        }

        return "";
    }

}
