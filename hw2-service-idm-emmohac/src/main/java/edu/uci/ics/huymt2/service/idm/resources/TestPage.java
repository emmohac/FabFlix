package edu.uci.ics.huymt2.service.idm.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.HashPassRequestModel;
import edu.uci.ics.huymt2.service.idm.models.HashedPassResponseModel;
import edu.uci.ics.huymt2.service.idm.models.SessionModel;
import edu.uci.ics.huymt2.service.idm.models.SessionRequestModel;
import edu.uci.ics.huymt2.service.idm.security.Crypto;
import edu.uci.ics.huymt2.service.idm.security.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

@Path("test")
public class TestPage {
    @Path("session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response getToken(String jsonText){
        ServiceLogger.LOGGER.info("Received request for session");
        ServiceLogger.LOGGER.info("Request:\n " + jsonText);

        ObjectMapper mapper = new ObjectMapper();
        SessionRequestModel rsm = null;
        SessionModel sessionModel = null;

        try {
            rsm = mapper.readValue(jsonText, SessionRequestModel.class);
            ServiceLogger.LOGGER.info("Email: " + rsm.getEmail());
            Session session = Session.createSession(rsm.getEmail());
            sessionModel = new SessionModel(session.getEmail(), session.getSessionID().toString());

        }catch (IOException e){
            ServiceLogger.LOGGER.info("IOException");
            e.printStackTrace();
        }
        ServiceLogger.LOGGER.info("Return session: " + sessionModel);
        return Response.status(Response.Status.OK).entity(sessionModel).build();
    }

    @Path("hashedPass")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response hashPassword(String jsonText){
        ServiceLogger.LOGGER.info("Received request to hash password.");
        ServiceLogger.LOGGER.info("Request: "+jsonText);
        ObjectMapper mapper = new ObjectMapper();
        HashPassRequestModel requestModel = null;
        HashedPassResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, HashPassRequestModel.class);
            char[] pword = requestModel.getPassword().toCharArray();
            byte[] salt = Crypto.genSalt();
            byte[] hashedPassword = Crypto.hashPassword(pword,salt,Crypto.ITERATIONS, Crypto.KEY_LENGTH);
            String password = getHashedPass(hashedPassword);
            responseModel = new HashedPassResponseModel(requestModel.getPassword(), password);
        }catch (IOException e){
            ServiceLogger.LOGGER.info("IOException");
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    private String getHashedPass(byte[] hashedPassword) {
        StringBuffer buf = new StringBuffer();
        for (byte b : hashedPassword) {
            buf.append(format(Integer.toHexString(Byte.toUnsignedInt(b))));
        }
        return buf.toString();
    }

    private String format(String binS) {
        int length = 2 - binS.length();
        char[] padArray = new char[length];
        Arrays.fill(padArray, '0');
        String padString = new String(padArray);
        return padString + binS;
    }

}
