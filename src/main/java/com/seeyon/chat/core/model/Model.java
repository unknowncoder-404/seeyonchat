package com.seeyon.chat.core.model;

import java.util.Map;

/**
 * @author Shaozz
 */
public class Model {

    private String model;

    private Map<String, String> name;

    private Map<String, String> description;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public String findName() {
        return name == null ? null : name.get("zh-CN");
    }

    public String findDescription() {
        return description == null ? null : description.get("zh-CN");
    }
}
