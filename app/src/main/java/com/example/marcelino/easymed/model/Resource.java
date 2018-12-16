package com.example.marcelino.easymed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class Resource {

    String uuid;
    String description;
    String [] capabilities;
    String status;
    Double lat;
    Double lon;
    String country;
    String state;
    String city;
    String neighborhood;
    String postal_code;
    String created_at;
    String updated_at;
    Long id;
    Long collect_interval;
    String uri;
    Double distance; // pesquisa o raio em metros, retorna em quilometros
    String bearing;

    public List<String> getCapabilitiesAsList(){
        return Arrays.asList(capabilities);
    }

    @Override
    public String toString(){
        return this.description + "\n"+"Distância: "+ this.distance + " Km";
    }
}
