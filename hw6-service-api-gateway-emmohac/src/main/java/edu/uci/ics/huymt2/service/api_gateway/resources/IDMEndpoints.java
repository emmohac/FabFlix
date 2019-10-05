package edu.uci.ics.huymt2.service.api_gateway.resources;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.*;
import edu.uci.ics.huymt2.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("idm")
public class IDMEndpoints {
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("IDMEP:: Received request to register user.");

        RegisterRequestModel requestModel;
        try{
            requestModel = (RegisterRequestModel) ModelValidator.verifyModel(jsonText, RegisterRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RegisterResponseModel.class);
        }

        ServiceLogger.LOGGER.info("Received request to login for email: "+requestModel.getEmail()+" with password: "+requestModel.getPassword());
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest clientRequest = new ClientRequest(transactionID, requestModel,
                GatewayService.getIdmConfigs().getIdmUri(),
                GatewayService.getIdmConfigs().getEPUserRegister());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("transactionID", transactionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                entity(responseModel).build();
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUserRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("IDMEP:: Received request to login.");
        LoginRequestModel requestModel;

        try{
            requestModel = (LoginRequestModel) ModelValidator.verifyModel(jsonText, LoginRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, LoginResponseModel.class);
        }

        ServiceLogger.LOGGER.info("Received request to login for email: "+requestModel.getEmail()+" with password: "+requestModel.getPassword());
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ClientRequest clientRequest = new ClientRequest(transactionID, requestModel,
                GatewayService.getIdmConfigs().getIdmUri(),
                GatewayService.getIdmConfigs().getEPUserLogin());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("transactionID", transactionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                entity(responseModel).build();
    }

    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifySessionRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("IDMEP:: Received request to verify session");

        VerifySessionRequestModel requestModel;

        try{
            requestModel = (VerifySessionRequestModel) ModelValidator.verifyModel(jsonText, VerifySessionRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, VerifySessionResponseModel.class);
        }

        String transactionID = TransactionIDGenerator.generateTransactionID();
        String email = headers.getHeaderString("email");

        ClientRequest clientRequest = new ClientRequest(transactionID, requestModel,
                GatewayService.getIdmConfigs().getIdmUri(),
                GatewayService.getIdmConfigs().getEPSessionVerify());

        clientRequest.setEmail(email);

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("transactionID", transactionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                entity(responseModel).build();
    }

    @Path("privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserPrivilegeRequest(@Context HttpHeaders headers,String jsonText) {
        ServiceLogger.LOGGER.info("IDMEP:: Received request to verify privilege");

        VerifyPrivilegeRequestModel requestModel;
        String transactionID = TransactionIDGenerator.generateTransactionID();
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");


        try{
            requestModel = (VerifyPrivilegeRequestModel) ModelValidator.verifyModel(jsonText, VerifyPrivilegeRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, VerifySessionResponseModel.class);
        }

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).entity(rm).build();

        String newSessionID = rm.getSessionID();

        ClientRequest clientRequest = new ClientRequest(transactionID, requestModel, GatewayService.getIdmConfigs().getIdmUri(), GatewayService.getIdmConfigs().getEPUserPrivilegeVerify());

        clientRequest.setEmail(email);

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        clientRequest.setSessionID(newSessionID);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel= new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("transactionID", transactionID).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                entity(responseModel).build();
    }
}
