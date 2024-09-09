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

    private final JTextField uriTextField  = new JTextField() ;
    private final JTextField apiKeyTextField  = new JTextField() ;
    private final JTextField modelTextField  = new JTextField() ;
    private final JTextField apiVersionTextField  = new JTextField() ;

    private final JTextArea splitCodePromptTextArea;
    private final JTextArea codeMergePromptTextArea;
    private final JTextArea javaCodepromptTextArea;

    public AppSettingsComponent() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(ChatConstants.MODEL_MAP.keySet().toArray(new String[0]));
        modelCombobox = new ComboBox<>(comboBoxModel);

        // 初始化多行文本框
        splitCodePromptTextArea = new JTextArea(5, 20);  // 设置文本框的行数和列数
        splitCodePromptTextArea.setLineWrap(true);  // 自动换行
        splitCodePromptTextArea.setWrapStyleWord(true);  // 以单词为单位换行

        JScrollPane splitCodePromptTextAreaScrollPane = new JScrollPane(splitCodePromptTextArea);  // 添加滚动条

        // 初始化多行文本框
        codeMergePromptTextArea = new JTextArea(5, 20);  // 设置文本框的行数和列数
        codeMergePromptTextArea.setLineWrap(true);  // 自动换行
        codeMergePromptTextArea.setWrapStyleWord(true);  // 以单词为单位换行
        JScrollPane codeMergePromptTextAreaScrollPane = new JScrollPane(codeMergePromptTextArea);  // 添加滚动条


        // 初始化多行文本框
        javaCodepromptTextArea = new JTextArea(5, 20);  // 设置文本框的行数和列数
        javaCodepromptTextArea.setLineWrap(true);  // 自动换行
        javaCodepromptTextArea.setWrapStyleWord(true);  // 以单词为单位换行
        JScrollPane javaCodepromptTextAreaScrollPane = new JScrollPane(javaCodepromptTextArea);  // 添加滚动条


        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("API key"), apiKeyField)
                .addLabeledComponent(new JBLabel("Model"), modelCombobox)

                .addLabeledComponent(new JBLabel("Azure Uri"), uriTextField)
                .addLabeledComponent(new JBLabel("Azure API Key"), apiKeyTextField)
                .addLabeledComponent(new JBLabel("Azure Deployment Id"), modelTextField)
                .addLabeledComponent(new JBLabel("Azure API Version"), apiVersionTextField)
                .addLabeledComponent(new JBLabel("代码拆分提示词模版:"), splitCodePromptTextAreaScrollPane)
                .addLabeledComponent(new JBLabel("json对象合并提示词模版:"), codeMergePromptTextAreaScrollPane)
                .addLabeledComponent(new JBLabel("生成单元测试提示词模版:"), javaCodepromptTextAreaScrollPane)
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

    public String getSplitCodePromptText() {
        return splitCodePromptTextArea.getText();
    }

    public void setSplitCodePromptText(String promptText) {
        splitCodePromptTextArea.setText(promptText);
    }


    public String getCodeMergePromptText() {
        return codeMergePromptTextArea.getText();
    }

    public void setCodeMergePromptText(String promptText) {
        codeMergePromptTextArea.setText(promptText);
    }

    public String getJavaCodepromptText() {
        return javaCodepromptTextArea.getText();
    }

    public void setJavaCodepromptText(String promptText) {
        javaCodepromptTextArea.setText(promptText);
    }


    public String getUriTextFieldText() {
        return uriTextField.getText();
    }

    public void setUriTextFieldText(String text) {
        uriTextField.setText(text);
    }

    public String getApiKeyTextFieldText() {
        return apiKeyTextField.getText();
    }

    public void setApiKeyTextFieldText(String text) {
        apiKeyTextField.setText(text);
    }

    public String getModelTextFieldText() {
        return modelTextField.getText();
    }

    public void setModelTextFieldText(String text) {
        modelTextField.setText(text);
    }

    public String getApiVersionTextFieldText() {
        return apiVersionTextField.getText();
    }

    public void setApiVersionTextFieldText(String text) {
        apiVersionTextField.setText(text);
    }

}
