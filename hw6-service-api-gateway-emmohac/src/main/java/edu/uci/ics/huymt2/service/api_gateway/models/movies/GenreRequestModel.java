package edu.uci.ics.huymt2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;
@JsonIgnoreProperties(value = {"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreRequestModel extends RequestModel {
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
