package edu.uci.ics.huymt2.service.billing.resources;

import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("test")
public class TestPage {
    @Path("hello")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloWorld(){
        ServiceLogger.LOGGER.info("Receive request from frontend");
        String response = "Hello World";

        return Response.status(Status.OK).entity(response).build();
    }
}
