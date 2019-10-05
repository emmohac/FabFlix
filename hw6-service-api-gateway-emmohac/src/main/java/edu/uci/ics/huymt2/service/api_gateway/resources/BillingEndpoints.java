package edu.uci.ics.huymt2.service.api_gateway.resources;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard.CreditCardRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard.CreditCardResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard.DeleteCCardRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard.RetrieveCCardResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.customer.CustomerRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.customer.CustomerResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.customer.RetrieveCustomerRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.customer.RetrieveCustomerResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.order.OrderPlaceResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.order.OrderRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.order.OrderRetrieveResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.shoppingcart.*;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifySessionResponseModel;
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

@Path("billing")
public class BillingEndpoints {
    @Path("cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertToCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to insert shopping cart");

        ShoppingCartRequestModel requestModel;
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, ShoppingCartResponseModel.class);
        }
        ServiceLogger.LOGGER.info("BillingEP:: sessionID is not null");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for cart insert...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).entity(rm).build();

        String newSessionID = rm.getSessionID();

        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for cart insert: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCartInsert());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to insert shopping cart");
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        ShoppingCartRequestModel requestModel;
        try{
            requestModel = (ShoppingCartRequestModel) ModelValidator.verifyModel(jsonText, ShoppingCartRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, ShoppingCartResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for cart update...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for cart update: "+newSessionID);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCartUpdate());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to delete shopping cart");
        DeleteSCartRequestModel requestModel;
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (DeleteSCartRequestModel) ModelValidator.verifyModel(jsonText, DeleteSCartRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, ShoppingCartResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for cart delete...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for cart delete: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCartDelete());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to retrieve shopping cart.");
        RetrieveSCartRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (RetrieveSCartRequestModel) ModelValidator.verifyModel(jsonText, RetrieveSCartRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RetrieveCCardResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for cart retrieve...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for cart retrieve: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCartRetrieve());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCartRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to clear shopping cart.");
        RetrieveSCartRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (RetrieveSCartRequestModel) ModelValidator.verifyModel(jsonText, RetrieveSCartRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RetrieveSCartResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for cart clear...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCartClear());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("creditcard/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: received request to insert credit card.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        CreditCardRequestModel requestModel;

        try{
            requestModel = (CreditCardRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for creditcard insert...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);

        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for creditcard insert: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCcInsert());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        //return the new sessionID;
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("creditcard/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: received request to update credit card.");
        CreditCardRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (CreditCardRequestModel) ModelValidator.verifyModel(jsonText, CreditCardRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for creditcard update...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for creditcard update: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCcUpdate());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("creditcard/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: received request to delete credit card.");
        DeleteCCardRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (DeleteCCardRequestModel) ModelValidator.verifyModel(jsonText, DeleteCCardRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CreditCardResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for creditcard delete...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCcDelete());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("creditcard/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to retrieve credit card.");
        DeleteCCardRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (DeleteCCardRequestModel) ModelValidator.verifyModel(jsonText, DeleteCCardRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RetrieveCCardResponseModel.class);
        }
        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for creditcard retrieve...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for creditcard retrieve: "+newSessionID);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCcRetrieve());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("customer/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to insert a customer");
        CustomerRequestModel requestModel;

        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (CustomerRequestModel) ModelValidator.verifyModel(jsonText, CustomerRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CustomerResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for customer insert...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for customer insert: "+newSessionID);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCustomerInsert());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("customer/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to update a customer");
        CustomerRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (CustomerRequestModel) ModelValidator.verifyModel(jsonText, CustomerRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, CustomerResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for customer update...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCustomerUpdate());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("customer/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to retrieve a customer");
        RetrieveCustomerRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (RetrieveCustomerRequestModel) ModelValidator.verifyModel(jsonText, RetrieveCustomerRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RetrieveCustomerResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for customer retrieve...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("BillingEndPoint:: new sessionID for customer retrieve: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPCustomerRetrieve());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to place an order.");
        OrderRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (OrderRequestModel) ModelValidator.verifyModel(jsonText, OrderRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, OrderPlaceResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for order place...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).entity(rm).build();

        String newSessionID = rm.getSessionID();

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPOrderPlace());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);
        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrderRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("BillingEP:: Received request to retrieve an order.");
        OrderRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        try{
            requestModel = (OrderRequestModel) ModelValidator.verifyModel(jsonText, OrderRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, OrderRetrieveResponseModel.class);
        }

        ServiceLogger.LOGGER.info("BillingEP:: SessionID is not null..");
        ServiceLogger.LOGGER.info("BillingEP:: Verifying email and session for order retrieve...");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("sessionID", sessionID).
                    header("transactionID", transactionID).
                    header("email", email).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getBillingConfigs().getBillingUri(),
                GatewayService.getBillingConfigs().getEPOrderRetrieve());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("requestDelay", GatewayService.getGatewayConfigs().getRequestDelay()).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }
}
