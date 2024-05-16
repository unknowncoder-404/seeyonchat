package com.seeyon.chat.settings;

import javax.swing.*;
import java.util.Map;

/**
 * @author Shaozz
 */
public class Constant {

    public static final Map<String, String> modelMap = Map.of(
            "GPT-3.5-Turbo", "gpt-3.5-turbo",
            "GPT-4", "gpt-4v"
    );

    public static final ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(modelMap.keySet().toArray(new String[0]));

}
