package com.seeyon.chat.common;

import com.intellij.openapi.application.ApplicationInfo;

import java.util.Map;

/**
 * @author Shaozz
 */
public class ChatConstants {

    public static final String BASE_URL = "https://seeyon.chat/api";

    public static final Map<String, String> MODEL_MAP = Map.of(
            "GPT-3.5-Turbo", "gpt-3.5-turbo",
            "GPT-4", "gpt-4v"
    );

    public static final String IDE_NAME = ApplicationInfo.getInstance().getVersionName();

}
