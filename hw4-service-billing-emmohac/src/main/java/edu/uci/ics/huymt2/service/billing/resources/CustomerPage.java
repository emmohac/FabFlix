package edu.uci.ics.huymt2.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.logicalhandler.Customer;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.models.creditcard.RetrieveCCardResponseModel;
import edu.uci.ics.huymt2.service.billing.models.customer.CustomerRequestModel;
import edu.uci.ics.huymt2.service.billing.models.customer.CustomerResponseModel;
import edu.uci.ics.huymt2.service.billing.models.customer.RetrieveCustomerRequestModel;
import edu.uci.ics.huymt2.service.billing.models.customer.RetrieveCustomerResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Path("customer")
public class CustomerPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to insert customer.");
        CustomerRequestModel requestModel;
        CustomerResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);
        try{
            requestModel = mapper.readValue(jsonText, CustomerRequestModel.class);
            responseModel = Customer.insertCustomerFrom(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.SUCCESSFULLY_INSERTED_CUSTOMER || resultCode == ResultCode.CREDITCARD_NOT_FOUND
            || resultCode == ResultCode.DUPLICATE_CUSTOMER_INSERT || resultCode == ResultCode.INVALID_CREDITCARD_LENGTH
            || resultCode == ResultCode.INVALID_CREDITCARD_VALUE)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new CustomerResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new CustomerResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to update customer.");
        CustomerRequestModel requestModel;
        CustomerResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);
        try{
            requestModel = mapper.readValue(jsonText, CustomerRequestModel.class);
            responseModel = Customer.updateCustomerFrom(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.SUCCESSFULLY_UPDATED_CUSTOMER || resultCode == ResultCode.CREDITCARD_NOT_FOUND
                    || resultCode == ResultCode.INVALID_CREDITCARD_LENGTH || resultCode == ResultCode.INVALID_CREDITCARD_VALUE
            || resultCode == ResultCode.CUSTOMER_NOT_EXIST)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new CustomerResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new CustomerResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve customer.");
        RetrieveCustomerRequestModel requestModel;
        RetrieveCustomerResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        try{
            requestModel = mapper.readValue(jsonText, RetrieveCustomerRequestModel.class);
            responseModel = Customer.retrieveCustomerFromDB(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.SUCCESSFULLY_RETRIEVED_CUSTOMER || resultCode == ResultCode.CUSTOMER_NOT_EXIST)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new RetrieveCCardResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new RetrieveCCardResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
