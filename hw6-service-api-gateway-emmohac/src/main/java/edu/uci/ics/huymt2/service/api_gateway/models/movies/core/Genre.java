package edu.uci.ics.huymt2.service.api_gateway.models.movies.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Genre {
    @JsonProperty(required = true)
    private int id;
    @JsonProperty(required = true)
    private String name;

    public Genre(){}

    @JsonCreator
    public Genre(@JsonProperty(value = "id", required = true) int id,
                 @JsonProperty(value = "name", required = true) String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty(value = "id")
    public int getId() {
        return id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}
