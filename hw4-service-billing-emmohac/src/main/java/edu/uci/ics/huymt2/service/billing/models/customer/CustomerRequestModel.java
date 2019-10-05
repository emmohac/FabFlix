package edu.uci.ics.huymt2.service.billing.models.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerRequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String firstName;
    @JsonProperty(required = true)
    private String lastName;
    @JsonProperty(required = true)
    private String ccId;
    @JsonProperty(required = true)
    private String address;

    public CustomerRequestModel() {}

    @JsonCreator
    public CustomerRequestModel(@JsonProperty(value = "email", required = true) String email,
                                @JsonProperty(value = "firstName", required = true) String firstName,
                                @JsonProperty(value = "lastName", required = true) String lastName,
                                @JsonProperty(value = "ccId", required = true) String ccId,
                                @JsonProperty(value = "address", required = true) String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty(value = "lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty(value = "ccId")
    public String getCcId() {
        return ccId;
    }

    @JsonProperty(value = "address")
    public String getAddress() {
        return address;
    }
}
