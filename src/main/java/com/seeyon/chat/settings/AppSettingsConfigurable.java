package com.seeyon.chat.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.Strings;
import com.seeyon.chat.utils.ChatHttpUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Shaozz
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent appSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "";
    }

    @Override
    public @Nullable JComponent createComponent() {
        appSettingsComponent = new AppSettingsComponent();
        return appSettingsComponent.getPanel();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return appSettingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        String apiKeyText = appSettingsComponent.getApiKeyText();
        return Strings.isNotEmpty(apiKeyText) &&
                (!apiKeyText.equals(settings.getApiKey()) ||
                        !appSettingsComponent.getModelSelectedItem().equals(settings.getModel()));
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.setApiKey(appSettingsComponent.getApiKeyText());
        settings.setModel(appSettingsComponent.getModelSelectedItem());
        if (Strings.isNotEmpty(settings.getApiKey()) && Strings.isEmpty(settings.getChatbotId())) {
            try {
                // create chatbot
                String chatbotId = ChatHttpUtil.createChatbot(settings.findModel());
                settings.putChatbotId(chatbotId);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        appSettingsComponent.setApiKeyText(settings.getApiKey());
        appSettingsComponent.setModelSelectedItem(settings.getModel());
    }

    @Override
    public void disposeUIResources() {
        appSettingsComponent = null;
    }
}
