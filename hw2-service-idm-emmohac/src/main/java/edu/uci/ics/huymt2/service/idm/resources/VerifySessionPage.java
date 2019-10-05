package edu.uci.ics.huymt2.service.idm.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.idm.core.HelpMe;
import edu.uci.ics.huymt2.service.idm.core.ResultCode;
import edu.uci.ics.huymt2.service.idm.core.ValidateSession;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.VerifySessionRequestModel;
import edu.uci.ics.huymt2.service.idm.models.VerifySessionResponseModel;

@Path("session")
public class VerifySessionPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sessionCheckRequest(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Received request to check session status");
        ServiceLogger.LOGGER.info(jsonText);
        ObjectMapper mapper = new ObjectMapper();
        VerifySessionRequestModel requestModel;
        VerifySessionResponseModel responseModel;

        try{
            requestModel = mapper.readValue(jsonText, VerifySessionRequestModel.class);
            responseModel = ValidateSession.validate(requestModel);
            int resultCode = responseModel.getResultCode();
            ServiceLogger.LOGGER.info("ResultCode when verify session is: " + resultCode);
            boolean isOK = HelpMe.verifyResultCode(resultCode);
            ServiceLogger.LOGGER.info("Is ResultCode OK: "+isOK);
            if (isOK)
                return Response.status(Status.OK).entity(responseModel).build();
            else
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            if (resultCode == -13 || resultCode == -11 || resultCode == -10){
//                ServiceLogger.LOGGER.info("VerifySessionPage fails at -13, -11 or -10.");
//                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            }
//            if (resultCode == 14 || resultCode == 130 || resultCode == 131 || resultCode == 132 || resultCode == 133 || resultCode == 134){
//                ServiceLogger.LOGGER.info("VerifySessionPage at 14, 130, 131, 132, 133");
//                return Response.status(Status.OK).entity(responseModel).build();
//            }

        }catch (IOException e){
            if (e instanceof JsonParseException){
                ServiceLogger.LOGGER.info("VerifySessionPage: json parseexception.");
                return Response.status(Status.BAD_REQUEST).entity(new VerifySessionResponseModel(ResultCode.JSON_MAP)).build();
            }
            else if (e instanceof JsonMappingException){
                ServiceLogger.LOGGER.info("VerifySessionPage: json mappingexception");
                return Response.status(Status.BAD_REQUEST).entity(new VerifySessionResponseModel(ResultCode.JSON_PARSE)).build();
            }
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
