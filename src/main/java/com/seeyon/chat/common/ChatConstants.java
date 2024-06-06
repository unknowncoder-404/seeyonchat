package com.seeyon.chat.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Shaozz
 */
public class ChatConstants {

    public static final String BASE_URL = "https://seeyon.chat/api";

    public static final Map<String, String> MODEL_MAP = new LinkedHashMap<>();

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

//    public static final String IDE_NAME = ApplicationInfo.getInstance().getVersionName();

    static {
        MODEL_MAP.put("GPT-3.5-Turbo", "gpt-3.5-turbo");
        MODEL_MAP.put("GPT-4", "gpt-4");
        MODEL_MAP.put("GPT-4V", "gpt-4v");
        MODEL_MAP.put("通义千问", "qwen");
        MODEL_MAP.put("文心一言", "wenxin");
        MODEL_MAP.put("ChatGLM", "chatglm");
        MODEL_MAP.put("Llama 3 70B", "tai-meta-llama/Llama-3-8b-chat-hf");

        // 配置ObjectMapper在反序列化时忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
