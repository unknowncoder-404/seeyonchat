package com.seeyon.chat.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeyon.chat.core.model.Model;
import com.seeyon.chat.utils.ChatHttpUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shaozz
 */
public class ChatConstants {

    public static final String BASE_URL = "https://seeyon.chat/api";

    private static final Map<String, Model> MODEL_MAP = new LinkedHashMap<>();

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

//    public static final String IDE_NAME = ApplicationInfo.getInstance().getVersionName();

    static {
        // 配置ObjectMapper在反序列化时忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Collection<Model> getModels() {
        if (MODEL_MAP.isEmpty()) {
            try {
                List<Model> models = ChatHttpUtil.getModels();
                for (Model model : models) {
                    MODEL_MAP.put(model.getModel(), model);
                }
            } catch (Exception ignored) {
            }
        }
        return MODEL_MAP.values();
    }

    public static Model getModel(String model) {
        return MODEL_MAP.get(model);
    }

}
