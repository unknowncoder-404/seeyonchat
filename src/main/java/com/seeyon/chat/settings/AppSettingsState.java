package com.seeyon.chat.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.seeyon.chat.common.ChatConstants;
import org.jetbrains.annotations.NotNull;

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
@Service
public final class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    private String apiKey = "";

    private String model = "";

    // key:model value:chatbotId
    public final Map<String, String> chatbotMap = new HashMap<>();

    @Transient
    private String chatId;

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

    @Transient
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
