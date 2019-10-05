package edu.uci.ics.huymt2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Genres;
import edu.uci.ics.huymt2.service.movies.models.GenreRequestModel;
import edu.uci.ics.huymt2.service.movies.models.MovieResponseModel;
import edu.uci.ics.huymt2.service.movies.models.VerifyPrivilegeResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("genre")
public class GenrePage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGenreListRequest(@Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Received request to retrieve all genre.");
        String email = headers.getHeaderString("email");

        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();

        ServiceLogger.LOGGER.info("GenrePage:: user has sufficient privilege level.");

        MovieResponseModel responseModel = Genres.retrieveAllGenreFromDB();
        int resultCode = responseModel.getResultCode();
        if (resultCode == ResultCode.SUCCESSFULLY_RETRIEVED_GENRE)
            return Response.status(Status.OK).entity(responseModel).build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers, String jsonText){
        String email = headers.getHeaderString("email");

        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();

        ServiceLogger.LOGGER.info("GenrePage:: user has sufficient privilege level.");

        GenreRequestModel requestModel;
        MovieResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        try{
            requestModel = mapper.readValue(jsonText, GenreRequestModel.class);
            responseModel = Genres.addGenreToDB(requestModel);
            int resultCode = responseModel.getResultCode();
            if (resultCode == ResultCode.GENRE_NOT_ADDED || resultCode == ResultCode.SUCCESSFULLY_ADDED_GENRE)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new MovieResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new MovieResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("GenrePage:: IOException");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    @Path("{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenreOfMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid){
        String email = headers.getHeaderString("email");

        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();

        ServiceLogger.LOGGER.info("GenrePage:: user has sufficient privilege level.");

        MovieResponseModel responseModel = Genres.retrieveGenreOfMovie(movieid);

        int resultCode = responseModel.getResultCode();
        if (resultCode == ResultCode.SUCCESSFULLY_RETRIEVED_GENRE || resultCode == ResultCode.MOVIE_NOT_FOUND)
            return Response.status(Status.OK).entity(responseModel).build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
