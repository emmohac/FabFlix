package edu.uci.ics.huymt2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Ratings;
import edu.uci.ics.huymt2.service.movies.models.RatingRequestModel;
import edu.uci.ics.huymt2.service.movies.models.RatingResponseModel;

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

@Path("rating")
public class RatingPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers, String jsonText){
        RatingRequestModel requestModel;
        RatingResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        try{
            requestModel = mapper.readValue(jsonText, RatingRequestModel.class);
            responseModel = Ratings.updateRatingFrom(requestModel);
            int resultCode = responseModel.getResultCode();

            if (resultCode == ResultCode.MOVIE_NOT_FOUND || resultCode == ResultCode.RATING_NOT_UPDATED || resultCode == ResultCode.SUCCESSFULLY_UPDATED_RATING)
                return Response.status(Status.OK).entity(responseModel).build();
        }catch (IOException e){
            if (e instanceof JsonParseException)
                return Response.status(Status.BAD_REQUEST).entity(new RatingResponseModel(ResultCode.JSON_PARSE)).build();
            else if (e instanceof JsonMappingException)
                return Response.status(Status.BAD_REQUEST).entity(new RatingResponseModel(ResultCode.JSON_MAP)).build();
            else
                ServiceLogger.LOGGER.info("RatingPage:: IOException.");
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
