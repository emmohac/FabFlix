package edu.uci.ics.huymt2.service.movies.resources;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Movies;
import edu.uci.ics.huymt2.service.movies.models.MovieAddRequestModel;
import edu.uci.ics.huymt2.service.movies.models.MovieAddResponseModel;
import edu.uci.ics.huymt2.service.movies.models.VerifyPrivilegeResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("add")
public class MovieAddPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        ServiceLogger.LOGGER.info("Received request to add movie for email: "+email);
        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();

        ServiceLogger.LOGGER.info("MovieAddPage:: user has sufficient privilege level.");

        MovieAddRequestModel requestModel;
        ObjectMapper mapper = new ObjectMapper();
        MovieAddResponseModel responseModel;
        try{
            requestModel = mapper.readValue(jsonText, MovieAddRequestModel.class);
            responseModel = Movies.addMovieToDBFrom(requestModel);
            int resultCode = responseModel.getResultCode();

            if (resultCode == ResultCode.SUCCESSFULLY_ADDED_MOVIE || resultCode == ResultCode.MOVIE_ALREADY_EXISTED || resultCode == ResultCode.MOVIE_NOT_ADDED)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new MovieAddResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new MovieAddResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("MoviePage::IOException.");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
