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
import edu.uci.ics.huymt2.service.idm.core.ValidateLogin;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.LoginRequestModel;
import edu.uci.ics.huymt2.service.idm.models.LoginResponseModel;

@Path("login")
public class LoginPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLoginRequest(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Received request for login.");
        ServiceLogger.LOGGER.info("Request: "+jsonText);
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestModel requestModel;
        LoginResponseModel responseModel;

        try{
            requestModel = mapper.readValue(jsonText, LoginRequestModel.class);
            ServiceLogger.LOGGER.info("Receiving email: "+requestModel.getEmail());
            responseModel = ValidateLogin.validate(requestModel);
            int resultCode = responseModel.getResultCode();
            ServiceLogger.LOGGER.info("ResultCode when log in is: " + resultCode);
            boolean isOK = HelpMe.verifyResultCode(resultCode);
            ServiceLogger.LOGGER.info("Is ResultCode OK: "+isOK);
            if (isOK)
                return Response.status(Status.OK).entity(responseModel).build();
            else
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            if (resultCode == ResultCode.PASSWORD_INVALID_LENGTH || resultCode == ResultCode.EMAIL_INVALID_FORMAT
//                    || resultCode == ResultCode.EMAIL_INVALID_LENGTH){
//                ServiceLogger.LOGGER.info("Login BAD at "+resultCode);
//                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            }
//
//            if (resultCode == ResultCode.SUCCESSFULLY_LOGIN || resultCode == ResultCode.PASSWORD_NOT_MATCH
//                    || resultCode == ResultCode.USER_NOT_FOUND){
//                ServiceLogger.LOGGER.info("Login OK at "+resultCode);
//                return Response.status(Status.OK).entity(responseModel).build();
//            }
        }catch (IOException e){
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Status.BAD_REQUEST).entity(new LoginResponseModel(ResultCode.JSON_PARSE)).build();
            }
            else if (e instanceof JsonParseException){
                return Response.status(Status.BAD_REQUEST).entity(new LoginResponseModel(ResultCode.JSON_MAP)).build();
            }
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
