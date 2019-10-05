package edu.uci.ics.huymt2.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ShoppingCart;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.models.shoppingcart.*;

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

@Path("cart")
public class ShoppingCartPage {
    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shoppingCartInsertRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to insert shopping cart.");
        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);
            responseModel = ShoppingCart.insertSCartToDBFrom(requestModel);

            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_FORMAT || resultCode == ResultCode.INVALID_LENGTH)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(resultCode)).build();
            if (resultCode == ResultCode.DUPLICATE_INSERT || resultCode == ResultCode.INVALID_QUANTITY || resultCode == ResultCode.SUCCESSFULLY_INSERTED)
                return Response.status(Status.OK).entity(responseModel).build();

        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response shoppingCartUpdateRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to update shopping cart.");
        ShoppingCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, ShoppingCartRequestModel.class);
            responseModel = ShoppingCart.updateSCartToDBFrom(requestModel);

            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_LENGTH || resultCode == ResultCode.INVALID_FORMAT)
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            if (resultCode == ResultCode.INVALID_QUANTITY || resultCode == ResultCode.ITEM_NOT_EXISTED || resultCode == ResultCode.SUCCESSFULLY_UPDATED)
                return Response.status(Status.OK).entity(responseModel).build();

        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response shoppingCartDeleteRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to delete shopping cart");
        DeleteSCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, DeleteSCartRequestModel.class);
            responseModel = ShoppingCart.deleteSCartInDB(requestModel);

            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_FORMAT || resultCode == ResultCode.INVALID_LENGTH)
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            if (resultCode == ResultCode.ITEM_NOT_EXISTED || resultCode == ResultCode.ITEM_DELETED)
                return Response.status(Status.OK).entity(responseModel).build();

        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response shoppingCartRetrieveRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve shopping cart");
        RetrieveSCartRequestModel requestModel;
        RetrieveSCartResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, RetrieveSCartRequestModel.class);
            responseModel = ShoppingCart.retrieveSCartFromDB(requestModel);

            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.INVALID_FORMAT || resultCode == ResultCode.INVALID_LENGTH)
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            ServiceLogger.LOGGER.info("ShoppingCartPage::email OK");
            if (resultCode == ResultCode.ITEM_NOT_EXISTED || resultCode == ResultCode.ITEM_RETRIEVED)
                return Response.status(Status.OK).entity(responseModel).build();

        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new RetrieveSCartResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new RetrieveSCartResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response shoppingCartClearRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to clear shopping cart.");
        RetrieveSCartRequestModel requestModel;
        ShoppingCartResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, RetrieveSCartRequestModel.class);
            responseModel = ShoppingCart.clearSCartFromDB(requestModel);
            int resultCode = responseModel.getResultCode();

            if (resultCode == ResultCode.INVALID_FORMAT || resultCode == ResultCode.INVALID_LENGTH)
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
            ServiceLogger.LOGGER.info("ShoppingCartPage::email OK");
            if (resultCode == ResultCode.ITEM_CLEARED)
                return Response.status(Status.OK).entity(responseModel).build();

        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new ShoppingCartResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

}
