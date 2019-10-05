package edu.uci.ics.huymt2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.core.Genre;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreModel {
    @JsonProperty(required = true)
    private int id;
    @JsonProperty(required = true)
    private String name;

    public GenreModel(){}

    @JsonCreator
    public GenreModel(@JsonProperty(value = "id", required = true) int id,
                      @JsonProperty(value = "name", required = true) String name) {
        this.id = id;
        this.name = name;
    }

    public static GenreModel buildModelFromObject(Genre g){
        return new GenreModel(g.getId(), g.getName());
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
