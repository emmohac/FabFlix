package edu.uci.ics.huymt2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Stars;
import edu.uci.ics.huymt2.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("star")
public class StarPage {
    @Path("{starid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStarIDRequest(@Context HttpHeaders headers, @PathParam("starid") String starid){
        StarIDSearchResponseModel responseModel = Stars.retrieveStarID(starid);
        int resultCode = responseModel.getResultCode();
        if (resultCode == ResultCode.STAR_NOT_FOUND || resultCode == ResultCode.STAR_FOUND)
            return Response.status(Status.OK).entity(responseModel).build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarInMovieRequest(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();
        ServiceLogger.LOGGER.info("StarPage:: user has sufficient privilege level.");

        StarMovieRequestModel requestModel;
        StarMovieResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        try{
            requestModel = mapper.readValue(jsonText, StarMovieRequestModel.class);
            responseModel = Stars.addStarInMovieFrom(requestModel);
            int resultCode = responseModel.getResultCode();

            if (resultCode == ResultCode.STAR_MOVIE_NOT_ADDED || resultCode == ResultCode.SUCCESSFULLY_ADDED_STAR_MOVIE || resultCode == ResultCode.STAR_MOVIE_ALREADY_EXISTED || resultCode == ResultCode.MOVIE_NOT_FOUND)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new StarMovieResponseModel(ResultCode.JSON_MAP)).build();
            else if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new StarMovieResponseModel(ResultCode.JSON_PARSE)).build();
            else
                ServiceLogger.LOGGER.info("StarPage:: IOException");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");
        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();
        ServiceLogger.LOGGER.info("StarPage:: user has sufficient privilege level.");

        StarAddRequestModel requestModel;
        StarAddResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        try{
            requestModel = mapper.readValue(jsonText, StarAddRequestModel.class);
            responseModel = Stars.addStarFrom(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.SUCCESSFULLY_ADDED_STAR || resultCode == ResultCode.STAR_ALREADY_EXISTED || resultCode == ResultCode.STAR_NOT_ADDED)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new StarAddResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new StarAddResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("StarPage:: IOException.");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
