package com.example.marcelino.easymed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties
public class ContextData implements Serializable {

    String uuid;

    Map<String, List<Map<String, Object>>> capabilities;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, List<Map<String, Object>>> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, List<Map<String, Object>>> capabilities) {
        this.capabilities = capabilities;
    }

}
