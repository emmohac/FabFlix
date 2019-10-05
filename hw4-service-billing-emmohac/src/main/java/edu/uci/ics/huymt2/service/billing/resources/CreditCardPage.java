package edu.uci.ics.huymt2.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.billing.logicalhandler.CreditCard;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.models.creditcard.CreditCardRequestModel;
import edu.uci.ics.huymt2.service.billing.models.creditcard.CreditCardResponseModel;
import edu.uci.ics.huymt2.service.billing.models.creditcard.DeleteCCardRequestModel;
import edu.uci.ics.huymt2.service.billing.models.creditcard.RetrieveCCardResponseModel;

import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("creditcard")
public class CreditCardPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to insert credit card.");
        CreditCardRequestModel requestModel;
        CreditCardResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, CreditCardRequestModel.class);
            responseModel = CreditCard.insertCCardToDBFrom(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_CREDITCARD_VALUE || resultCode == ResultCode.INVALID_CREDITCARD_LENGTH
            || resultCode == ResultCode.SUCCESSFULLY_INSERTED_CREDITCARD || resultCode == ResultCode.DUPLICATE_CREDITCARD_INSERT
            || resultCode == ResultCode.INVALID_EXPIRATION)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new CreditCardResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new CreditCardResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("CreditCardPage::insert IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to update credit card.");
        CreditCardRequestModel requestModel;
        CreditCardResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);
        try{
            requestModel = mapper.readValue(jsonText, CreditCardRequestModel.class);
            responseModel = CreditCard.updateCCardToDBFrom(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_CREDITCARD_VALUE || resultCode == ResultCode.INVALID_CREDITCARD_LENGTH
                    || resultCode == ResultCode.SUCCESSFULLY_UPDATED_CREDITCARD || resultCode == ResultCode.CREDITCARD_NOT_EXISTED
                    || resultCode == ResultCode.INVALID_EXPIRATION)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new CreditCardResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new CreditCardResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("CreditCard::update IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to delete credit card.");
        DeleteCCardRequestModel requestModel;
        CreditCardResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);
        try{
            requestModel = mapper.readValue(jsonText, DeleteCCardRequestModel.class);
            responseModel = CreditCard.deleteCCardFromDB(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_CREDITCARD_VALUE || resultCode == ResultCode.INVALID_CREDITCARD_LENGTH
                    || resultCode == ResultCode.SUCCESSFULLY_DELETED_CREDITCARD || resultCode == ResultCode.CREDITCARD_NOT_EXISTED)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new CreditCardResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new CreditCardResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("CreditCard::delete IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve credit card.");
        DeleteCCardRequestModel requestModel;
        RetrieveCCardResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);
        try{
            requestModel = mapper.readValue(jsonText, DeleteCCardRequestModel.class);
            responseModel = CreditCard.retrieveCCardFromDB(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_CREDITCARD_VALUE || resultCode == ResultCode.INVALID_CREDITCARD_LENGTH
                    || resultCode == ResultCode.SUCCESSFULLY_RETRIEVED_CREDITCARD || resultCode == ResultCode.CREDITCARD_NOT_EXISTED)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new RetrieveCCardResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new RetrieveCCardResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("CreditCard::retrieve IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
