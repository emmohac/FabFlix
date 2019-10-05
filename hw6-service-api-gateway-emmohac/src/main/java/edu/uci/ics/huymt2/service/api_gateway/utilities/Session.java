package edu.uci.ics.huymt2.service.api_gateway.utilities;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifySessionRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifySessionResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Session {
    public static VerifySessionResponseModel verify(String email, String sessionID){
        ServiceLogger.LOGGER.info("Trying to verity email: "+email+" and sessionID: "+sessionID);
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        String IDM_URI = GatewayService.getIdmConfigs().getIdmUri();

        String IDM_ENDPOINT = GatewayService.getIdmConfigs().getEPSessionVerify();

        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT);

        Invocation.Builder invocation = webTarget.request(MediaType.APPLICATION_JSON);

        VerifySessionRequestModel requestModel = new VerifySessionRequestModel(email, sessionID);

        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocation.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));

        ServiceLogger.LOGGER.info("Sent!");

        VerifySessionResponseModel responseModel = response.readEntity(VerifySessionResponseModel.class);
        ServiceLogger.LOGGER.info("ResponseModel resultCode: "+responseModel.getResultCode());
        ServiceLogger.LOGGER.info("ResponseModel message: "+responseModel.getMessage());
        ServiceLogger.LOGGER.info("ResponseModel sessionID: "+responseModel.getSessionID());
        return responseModel;

    }
}
