package edu.uci.ics.huymt2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyPrivilegeRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private int plevel;

    public VerifyPrivilegeRequestModel(){}

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "plevel")
    public int getPlevel() {
        return plevel;
    }

    @JsonCreator
    public VerifyPrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                       @JsonProperty(value = "plevel", required = true) int plevel) {
        this.email = email;
        this.plevel = plevel;
    }
}
