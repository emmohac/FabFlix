package edu.uci.ics.huymt2.service.movies.resources;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Movies;
import edu.uci.ics.huymt2.service.movies.models.MovieSearchRequestModel;
import edu.uci.ics.huymt2.service.movies.models.MovieSearchResponseModel;
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


@Path("search")
public class MovieSearchPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers, @QueryParam("title") String title, @QueryParam("genre") String genre,
                                       @QueryParam("director") String director, @QueryParam("year") Integer year, @QueryParam("hidden") Boolean hidden,
                                       @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                       @QueryParam("orderby") String orderby, @QueryParam("direction") String direction){

        String email = headers.getHeaderString("email");
        ServiceLogger.LOGGER.info("Received request to retrieve movie of email: "+email);

        VerifyPrivilegeResponseModel rm = HelpMe.verifyUserPrivilege(email, ResultCode.EMPLOYEE);
        Boolean isHidden = false;

        int privilegeResultCode = rm.getResultCode();
        if (privilegeResultCode != ResultCode.SUFFICIENT_PRIVILEGE)
            isHidden = true;

        ServiceLogger.LOGGER.info("resultCode is: "+privilegeResultCode);
        ServiceLogger.LOGGER.info("hidden is: "+hidden);
        ServiceLogger.LOGGER.info("isHidden is: "+isHidden);
        ServiceLogger.LOGGER.info("Successfully verified privilege level.\n");

        ServiceLogger.LOGGER.info("Finding movie...");
        MovieSearchRequestModel requestModel = new MovieSearchRequestModel(title, genre, year, director, isHidden, limit, offset, orderby, direction);
        MovieSearchResponseModel responseModel = Movies.retrieveMovieFromDB(requestModel);
        int resultCode = responseModel.getResultCode();

        if (resultCode == ResultCode.MOVIE_FOUND || resultCode == ResultCode.MOVIE_NOT_FOUND)
            return Response.status(Status.OK).entity(responseModel).build();
        ServiceLogger.LOGGER.info("Movie not found...");
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
