package com.seeyon.chat.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.util.ui.FormBuilder;
import com.seeyon.chat.common.ChatConstants;
import com.seeyon.chat.core.model.Model;

import javax.swing.*;
import java.awt.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JBPasswordField apiKeyField = new JBPasswordField();
    private final JComboBox<Model> modelCombobox;

    public AppSettingsComponent() {
        modelCombobox = new ComboBox<>(ChatConstants.getModels().toArray(new Model[0]));
        // 设置自定义渲染器
        modelCombobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Model model) {
                    setText(model.findName());
                    setToolTipText(model.findDescription());
                }
                return this;
            }
        });

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

    public Model getModelSelectedItem() {
        return (Model) modelCombobox.getSelectedItem();
    }

    public void setModelSelectedItem(String model) {
        Model item = ChatConstants.getModel(model);
        if (item != null) {
            modelCombobox.setSelectedItem(item);
        }
    }
}
