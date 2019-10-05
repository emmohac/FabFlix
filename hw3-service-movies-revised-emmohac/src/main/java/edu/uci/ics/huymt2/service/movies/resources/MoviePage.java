package edu.uci.ics.huymt2.service.movies.resources;

import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Movies;
import edu.uci.ics.huymt2.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("delete/{movieid}")
public class MoviePage {
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid){
        String email = headers.getHeaderString("email");
        boolean isSufficient = HelpMe.verifyPrivilegeLevel(email);
        if (!isSufficient)
            return Response.status(Status.OK).entity(new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE)).build();
        ServiceLogger.LOGGER.info("MoviePage:: user has sufficient privilege level.");

        MovieResponseModel responseModel = Movies.removeMovieFromDB(movieid);
        int resultCode = responseModel.getResultCode();
        if (resultCode == ResultCode.MOVIE_NOT_REMOVED || resultCode == ResultCode.SUCCESSFULLY_REMOVED_MOVIE || resultCode == ResultCode.MOVIE_ALREADY_REMOVED)
            return Response.status(Status.OK).entity(responseModel).build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
