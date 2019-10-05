package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.movies.core.Star;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarModel {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private Integer birthYear;

    public StarModel() {}

    @JsonCreator
    public StarModel(@JsonProperty(value = "id", required = true) String id,
                @JsonProperty(value = "name", required = true) String name,
                @JsonProperty(value = "birthYear", required = true) Integer birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public static StarModel buildModelFromObject(Star s){
        return new StarModel(s.getId(), s.getName(), s.getBirthYear());
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
