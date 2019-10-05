package edu.uci.ics.huymt2.service.api_gateway.models.movies.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Star {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private Integer birthYear;

    public Star() {}

    @JsonCreator
    public Star(@JsonProperty(value = "id", required = true) String id,
                @JsonProperty(value = "name", required = true) String name,
                @JsonProperty(value = "birthYear", required = true) Integer birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }
}
