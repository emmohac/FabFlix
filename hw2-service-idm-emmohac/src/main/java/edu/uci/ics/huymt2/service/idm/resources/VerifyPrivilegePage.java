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
import edu.uci.ics.huymt2.service.idm.core.ValidatePrivilege;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.VerifyPrivilegeRequestModel;
import edu.uci.ics.huymt2.service.idm.models.VerifyPrivilegeResponseModel;
import edu.uci.ics.huymt2.service.idm.models.VerifySessionResponseModel;

@Path("privilege")
public class VerifyPrivilegePage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response privilegeCheckRequest(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Received request to check privilege level");
        ServiceLogger.LOGGER.info("Request: "+jsonText);
        ObjectMapper mapper = new ObjectMapper();
        VerifyPrivilegeRequestModel requestModel;
        VerifyPrivilegeResponseModel responseModel;

        try{
            requestModel = mapper.readValue(jsonText, VerifyPrivilegeRequestModel.class);
            ServiceLogger.LOGGER.info("Received: "+requestModel.getEmail());
            responseModel = ValidatePrivilege.validate(requestModel);

            int resultCode = responseModel.getResultCode();
            ServiceLogger.LOGGER.info("ResultCode when verify privilege is: " + resultCode);
            boolean isOK = HelpMe.verifyResultCode(resultCode);
            ServiceLogger.LOGGER.info("Is ResultCode OK: "+isOK);
            if (isOK)
                return Response.status(Status.OK).entity(responseModel).build();
            else
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            if (resultCode == ResultCode.USER_NOT_FOUND || resultCode == ResultCode.EMAIL_INVALID_FORMAT
//                    || resultCode == ResultCode.EMAIL_INVALID_LENGTH){
//                ServiceLogger.LOGGER.info("VerifyPrivilegePage fails at -14, -11, -10");
//                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            }
//
//            if (resultCode == ResultCode.SUFFICIENT_PRIVILEGE || resultCode == ResultCode.INSUFFICIENT_PRIVILEGE){
//                ServiceLogger.LOGGER.info("VerifyPrivilege good at 141, 140");
//                return Response.status(Status.OK).entity(responseModel).build();
//            }
        }catch(IOException e){
            e.printStackTrace();
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new VerifyPrivilegeResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new VerifyPrivilegeResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
