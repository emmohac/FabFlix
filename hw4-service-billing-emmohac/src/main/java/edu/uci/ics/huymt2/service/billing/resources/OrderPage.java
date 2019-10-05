package edu.uci.ics.huymt2.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.logicalhandler.Order;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.models.order.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.TimeZone;

@Path("order")
public class OrderPage {
    @Path("place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to place order");
        OrderRequestModel requestModel;
        OrderPlaceResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, OrderRequestModel.class);
            responseModel = Order.placeOrderFrom(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.SUCCESSFULLY_PLACED_ORDER || resultCode == ResultCode.CUSTOMER_NOT_EXIST
                    || resultCode == ResultCode.SCART_NOT_FOUND)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new OrderResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new OrderResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("OrderPage:: IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeOrderRequest(@QueryParam("paymentId") String paymentId,
                                         @QueryParam("token") String token,
                                         @QueryParam("PayerID") String PayerID){
        OrderCompleteRequestModel requestModel = new OrderCompleteRequestModel(paymentId, token, PayerID);
        OrderResponseModel responseModel = Order.completeOrderFrom(requestModel);
        int resultCode = responseModel.getResultCode();

        if (resultCode == ResultCode.PAYMENT_NOT_COMPLETED || resultCode == ResultCode.PAYMENT_COMPLETED || resultCode == ResultCode.TOKEN_NOT_FOUND)
            return Response.status(Status.OK).entity(responseModel).build();

        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(String jsonText){
        ServiceLogger.LOGGER.info("Received request to retrieve order");
        OrderRequestModel requestModel;
        OrderRetrieveResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        mapper.setDateFormat(dateFormat);

        try{
            requestModel = mapper.readValue(jsonText, OrderRequestModel.class);
            responseModel = Order.retrieveOrderFromDB(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.SUCCESSFULLY_RETREIVED_ORDER || resultCode == ResultCode.CUSTOMER_NOT_EXIST)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new OrderResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new OrderResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("OrderPage:: IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
