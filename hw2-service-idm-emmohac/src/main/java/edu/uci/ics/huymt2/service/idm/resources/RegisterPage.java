package edu.uci.ics.huymt2.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.idm.core.HelpMe;
import edu.uci.ics.huymt2.service.idm.core.ResultCode;
import edu.uci.ics.huymt2.service.idm.core.ValidateRegister;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.huymt2.service.idm.models.RegisterResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("register")
public class RegisterPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userRegisterRequest(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Received request to register user.");
        ObjectMapper mapper = new ObjectMapper();
        RegisterRequestModel requestModel;
        RegisterResponseModel responseModel;

        try {
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);
            responseModel = ValidateRegister.validate(requestModel);

            int resultCode = responseModel.getResultCode();
            ServiceLogger.LOGGER.info("ResultCode when register is: " + resultCode);
            boolean isOK = HelpMe.verifyResultCode(resultCode);
            ServiceLogger.LOGGER.info("Is ResultCode OK: "+isOK);
            if (isOK)
                return Response.status(Status.OK).entity(responseModel).build();
            else
                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();

//            if (resultCode == ResultCode.EMAIL_INVALID_LENGTH || resultCode == ResultCode.EMAIL_INVALID_FORMAT
//                    || resultCode == ResultCode.PASSWORD_INVALID_LENGTH){
//                ServiceLogger.LOGGER.info("Failed at code -10, -11, -12");
//                return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
//            }
//
//            if (resultCode == ResultCode.SUCCESSFULLY_REGISTERED || resultCode == 12 || resultCode == 13
//                    || resultCode == ResultCode.EMAIL_ALREADY_USED){
//                ServiceLogger.LOGGER.info("Success at code 110");
//                return Response.status(Status.OK).entity(responseModel).build();
//            }
        }catch(IOException e){
            e.printStackTrace();
            if (e instanceof JsonMappingException){
                return Response.status(Status.BAD_REQUEST).entity(new RegisterResponseModel(ResultCode.JSON_PARSE)).build();
            }
            else if (e instanceof JsonParseException){
                return Response.status(Status.BAD_REQUEST).entity(new RegisterResponseModel(ResultCode.JSON_MAP)).build();
            }
            else
                ServiceLogger.LOGGER.info("IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build(); //case -1
    }
}
