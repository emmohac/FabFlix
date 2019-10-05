package edu.uci.ics.huymt2.service.movies.resources;

import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;
import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.movies.logicalhandler.Stars;
import edu.uci.ics.huymt2.service.movies.models.StarSearchRequestModel;
import edu.uci.ics.huymt2.service.movies.models.StarSearchResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("star")
public class StarSearchPage {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStarRequest(@Context HttpHeaders headers, @QueryParam("name") String name, @QueryParam("birthYear") Integer birthYear,
                                      @QueryParam("movieTitle") String movieTitle, @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                      @QueryParam("orderby") String orderby, @QueryParam("direction") String direction){
        ServiceLogger.LOGGER.info("Received request to search star.");
        limit = HelpMe.verifyLimit(limit);
        offset = HelpMe.verifyOffset(limit, offset);
        direction = HelpMe.verifyDirection(direction);
        orderby = HelpMe.verifyOrderbyForStar(orderby);


        StarSearchRequestModel requestModel = new StarSearchRequestModel(name, birthYear, movieTitle, limit, offset, orderby, direction);
        StarSearchResponseModel responseModel = Stars.searchStarFrom(requestModel);
        int resultCode = responseModel.getResultCode();

        if (resultCode == ResultCode.STAR_FOUND || resultCode == ResultCode.STAR_NOT_FOUND)
            return Response.status(Response.Status.OK).entity(responseModel).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
