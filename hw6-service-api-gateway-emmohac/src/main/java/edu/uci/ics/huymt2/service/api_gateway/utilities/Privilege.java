package edu.uci.ics.huymt2.service.api_gateway.utilities;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifyPrivilegeRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifyPrivilegeResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifySessionRequestModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Privilege {
    public static VerifyPrivilegeResponseModel verify(String email, int plevel){
        ServiceLogger.LOGGER.info("Verifying privilege level with IDM...");

        // Create a new Client

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        String IDM_URI = GatewayService.getIdmConfigs().getIdmUri();

        String IDM_ENDPOINT = GatewayService.getIdmConfigs().getEPUserPrivilegeVerify();

        // Create a WebTarget to send a request at
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT);

        Invocation.Builder invocation = webTarget.request(MediaType.APPLICATION_JSON);

        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        // Send the request and save it to a Response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocation.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        VerifyPrivilegeResponseModel responseModel = response.readEntity(VerifyPrivilegeResponseModel.class);

        return responseModel;
    }
}
