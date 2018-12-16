package com.example.marcelino.easymed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
public class Collector implements Serializable {

    private List<String> uuids;
    private List<String> capabilities;

    public List<String> getUuids() {
        if (uuids == null) {
            uuids = new ArrayList<>();
        }
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }

    public List<String> getCapabilities() {
        if (capabilities == null) {
            capabilities = new ArrayList<>();
        }
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }
}
