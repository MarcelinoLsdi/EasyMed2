package com.example.marcelino.easymed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@JsonIgnoreProperties
public class ResourceData {

    private String uuid;
    private List<CapabilityData> capabilityDatas;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<CapabilityData> getCapabilityDatas() {
        if (capabilityDatas == null) {
            capabilityDatas = new ArrayList<>();
        }
        return capabilityDatas;
    }

    public void setCapabilityDatas(List<CapabilityData> capabilityDatas) {
        this.capabilityDatas = capabilityDatas;
    }
}
