package com.example.marcelino.easymed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties
public class DataHelper implements Serializable {

    List<ContextData> resources;

    public List<ContextData> getResources() {
        return resources;
    }

    public void setResources(List<ContextData> resources) {
        this.resources = resources;
    }
}
