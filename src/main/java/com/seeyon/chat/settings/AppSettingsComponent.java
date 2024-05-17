package com.seeyon.chat.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.util.ui.FormBuilder;
import com.seeyon.chat.common.ChatConstants;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JBPasswordField apiKeyField = new JBPasswordField();
    private final JComboBox<String> modelCombobox;

    public AppSettingsComponent() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(ChatConstants.MODEL_MAP.keySet().toArray(new String[0]));
        modelCombobox = new ComboBox<>(comboBoxModel);

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("API key"), apiKeyField)
                .addLabeledComponent(new JBLabel("Model"), modelCombobox)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return apiKeyField;
    }

    public String getApiKeyText() {
        return new String(apiKeyField.getPassword());
    }

    public void setApiKeyText(String apiKey) {
        apiKeyField.setText(apiKey);
    }

    public String getModelSelectedItem() {
        return (String) modelCombobox.getSelectedItem();
    }

    public void setModelSelectedItem(String model) {
        modelCombobox.setSelectedItem(model);
    }
}
