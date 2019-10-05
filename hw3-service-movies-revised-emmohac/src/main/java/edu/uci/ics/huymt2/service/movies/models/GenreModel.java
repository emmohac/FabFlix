package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.movies.core.Genre;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreModel {
    @JsonProperty(required = true)
    private Integer id;
    @JsonProperty(required = true)
    private String name;

    public GenreModel(){}

    @JsonCreator
    public GenreModel(@JsonProperty(value = "id", required = true) Integer id,
                      @JsonProperty(value = "name", required = true) String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public GenreModel(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public static GenreModel buildModelFromObject(Genre g){
        return new GenreModel(g.getId(), g.getName());
    }

    @JsonProperty(value = "id")
    public Integer getId() {
        return id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}
