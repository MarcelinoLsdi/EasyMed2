package com.example.marcelino.easymed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties
public class CapabilityData implements Serializable {

    private String value;
    private String timestamp;
    private Double lat;
    private Double lon;
    private Resource resource;
    private Double distance;

    public CapabilityData(Map<String, Object> capability) {
        try {

            this.value = (String) capability.get("value");
        } catch (Exception e) {
            this.value = String.valueOf((Double) capability.get("value"));
        }
        this.timestamp = (String) capability.get("date");
        this.lat = (Double) capability.get("lat");
        this.lon = (Double) capability.get("lon");
        Map<String, Object> resourceMap = (Map<String, Object>) capability.get("resource");

        Resource resource = new Resource();

        if (resourceMap != null) {
            for (String key : resourceMap.keySet()) {
                resource.setUuid((String) resourceMap.get("uuid"));
                resource.setDescription((String) resourceMap.get("description"));
                resource.setLat((Double) resourceMap.get("lat"));
                resource.setLon((Double) resourceMap.get("lon"));
            }
        }

        this.resource = resource;

    }

    public CapabilityData() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }


    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "CapabilityData{" +
                "value='" + value + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", resource=" + resource.getDescription() +
                ", distance=" + distance +
                '}';
    }
}
