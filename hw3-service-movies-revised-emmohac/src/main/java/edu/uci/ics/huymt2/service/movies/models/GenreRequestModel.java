package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreRequestModel {
    @JsonProperty(required = true)
    private String name;

    @JsonCreator
    public GenreRequestModel(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}
