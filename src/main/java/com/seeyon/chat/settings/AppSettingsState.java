package com.seeyon.chat.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.seeyon.chat.common.ChatConstants;
import com.seeyon.chat.utils.StringLLMUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.seeyon.chat.settings.AppSettingsState",
        storages = @Storage("SeeyonChatSettingsPlugin.xml")
)
@Service(Service.Level.APP)
public final class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    private String apiKey = "";

    private String model = "";

    private String gptUri = "";

    private String gptApiVersion = "";

    private String gptModel = "";

    private String gptApiKey = "";

    private String gptSplitCodePrompt = "";

    private String gptCodeMergePrompt = "";

    private String gptJavaCodeprompt = "";

    // key:model value:chatbotId
    public final Map<String, String> chatbotMap = new HashMap<>();

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String findModel() {
        return ChatConstants.MODEL_MAP.get(model);
    }

    public String getChatbotId() {
        return chatbotMap.get(model);
    }

    public void putChatbotId(String chatbotId) {
        chatbotMap.put(model, chatbotId);
    }

    public String getGptUri() {
        if (StringLLMUtil.isNotEmpty(gptUri)) {
            return gptUri;
        }
        return "https://ai-develop-azure-openai.openai.azure.com";
    }

    public void setGptUri(String gptUri) {
        this.gptUri = gptUri;
    }

    public String getGptApiVersion() {
        if (StringLLMUtil.isNotEmpty(gptApiVersion)) {
            return gptApiVersion;
        }
        return "2024-05-01-preview";
    }

    public void setGptApiVersion(String gptApiVersion) {
        this.gptApiVersion = gptApiVersion;
    }

    public String getGptModel() {
        if (StringLLMUtil.isNotEmpty(gptModel)) {
            return gptModel;
        }
        return "gpt-4o";
    }

    public void setGptModel(String gptModel) {
        this.gptModel = gptModel;
    }

    public String getGptApiKey() {
        if (StringLLMUtil.isNotEmpty(gptApiKey) ) {
            return gptApiKey;
        }
        return "";
    }

    public void setGptApiKey(String gptApiKey) {
        this.gptApiKey = gptApiKey;
    }

    public String getGptSplitCodePrompt() {
        if (StringLLMUtil.isNotEmpty(gptSplitCodePrompt)) {
            return gptSplitCodePrompt;
        }
        return ChatConstants.SPLITE_CODE_TEMPLATE;
    }

    public void setGptSplitCodePrompt(String gptSplitCodePrompt) {
        this.gptSplitCodePrompt = gptSplitCodePrompt;
    }

    public String getGptCodeMergePrompt() {
        if (StringLLMUtil.isNotEmpty(gptCodeMergePrompt)) {
            return gptCodeMergePrompt;
        }
        return ChatConstants.SPLITE_JSON_MERGE_TEMPLATE;
    }

    public void setGptCodeMergePrompt(String gptCodeMergePrompt) {
        this.gptCodeMergePrompt = gptCodeMergePrompt;
    }

    public String getGptJavaCodeprompt() {
        if (StringLLMUtil.isNotEmpty(gptJavaCodeprompt)) {
            return gptJavaCodeprompt;
        }
        return ChatConstants.GENERATE_TESTCASE_TEMPLATE;
    }

    public void setGptJavaCodeprompt(String gptJavaCodeprompt) {
        this.gptJavaCodeprompt = gptJavaCodeprompt;
    }
}
