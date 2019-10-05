package edu.uci.ics.huymt2.service.api_gateway.threadpool;

import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

import javax.ws.rs.core.MultivaluedMap;

public class ClientRequest {
    private String email;
    private String sessionID;
    private String transactionID;
    private RequestModel request;
    private String URI;
    private String endpoint;
    private int HTTPMethod;
    private MultivaluedMap<String, String> queryParams;

    public ClientRequest(String email, String sessionID, String transactionID, RequestModel request, String URI, String endpoint) {
        this.email = email;
        this.sessionID = sessionID;
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
    }

    public ClientRequest(String transactionID, RequestModel request, String URI, String endpoint) {
        this.transactionID = transactionID;
        this.request = request;
        this.URI = URI;
        this.endpoint = endpoint;
    }

    public ClientRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public RequestModel getRequest() {
        return request;
    }

    public String getURI() {
        return URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getHTTPMethod() {
        return HTTPMethod;
    }

    public void setHTTPMethod(int HTTPMethod) {
        this.HTTPMethod = HTTPMethod;
    }

    public void setQueryParams(MultivaluedMap<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public MultivaluedMap<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "email='" + email + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", transactionID='" + transactionID + '\'' +
                ", request=" + request +
                ", URI='" + URI + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", HTTPMethod=" + HTTPMethod +
                '}';
    }
}
