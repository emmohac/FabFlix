package edu.uci.ics.huymt2.service.api_gateway.models.billing.core;

import com.fasterxml.jackson.annotation.*;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "firstName", "lastName", "expiration"})
public class CreditCardInfo {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String firstName;
    @JsonProperty(required = true)
    private String lastName;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expiration;

    public CreditCardInfo() {}

    @JsonCreator
    public CreditCardInfo(@JsonProperty(value = "id", required = true) String id,
                          @JsonProperty(value = "firstName", required = true) String firstName,
                          @JsonProperty(value = "lastName", required = true) String lastName,
                          @JsonProperty(value = "expiration", required = true) Date expiration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    @JsonProperty(value = "firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty(value = "lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty(value = "expiration")
    public Date getExpiration() {
        return expiration;
    }
}
