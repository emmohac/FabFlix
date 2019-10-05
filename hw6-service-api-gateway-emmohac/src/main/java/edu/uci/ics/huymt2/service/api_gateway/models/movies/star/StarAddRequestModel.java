package edu.uci.ics.huymt2.service.api_gateway.models.movies.star;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarAddRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private Integer birthYear;

    public StarAddRequestModel() {}

    @JsonCreator
    public StarAddRequestModel(@JsonProperty(value = "name", required = true) String name,
                               @JsonProperty(value = "birthYear",required = true) Integer birthYear) {
        this.name = name;
        this.birthYear = birthYear;
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
