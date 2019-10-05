package edu.uci.ics.huymt2.service.movies.resources;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Movies;
import edu.uci.ics.huymt2.service.movies.models.MovieIDSearchResponseModel;
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

@Path("get/{movieid}")
public class MovieGetPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieByIDRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid){
        String email = headers.getHeaderString("email");
        ServiceLogger.LOGGER.info("Received request to retrieve movie of email: "+email+" for movieID: "+movieid);

        VerifyPrivilegeResponseModel rm = HelpMe.verifyUserPrivilege(email, ResultCode.SERVICE);
        int privilegeResultCode = rm.getResultCode();
        Boolean isHidden = false;
        if (privilegeResultCode != ResultCode.SUFFICIENT_PRIVILEGE) {
            isHidden = true;
            ServiceLogger.LOGGER.info("MovieGetPage:: user has insufficient privilege level.");
        }

        MovieIDSearchResponseModel responseModel = Movies.retrieveMovieID(movieid, isHidden);
        int resultCode = responseModel.getResultCode();
        if (resultCode == ResultCode.MOVIE_ALREADY_REMOVED && privilegeResultCode != ResultCode.SUFFICIENT_PRIVILEGE)
            return Response.status(Status.OK).entity(rm).build();
        if (resultCode == ResultCode.MOVIE_FOUND || resultCode == ResultCode.MOVIE_NOT_FOUND)
            return Response.status(Status.OK).entity(responseModel).build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
