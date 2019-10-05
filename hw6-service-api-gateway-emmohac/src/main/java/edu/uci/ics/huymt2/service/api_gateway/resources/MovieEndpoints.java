package edu.uci.ics.huymt2.service.api_gateway.resources;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.api_gateway.models.NoContentResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.core.Transaction;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifyPrivilegeResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.idm.VerifySessionResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.GenreRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.RatingRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.RatingResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.movie.*;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.star.StarAddRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.star.StarAddResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.star.StarMovieRequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.star.StarMovieResponseModel;
import edu.uci.ics.huymt2.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo) {
        String transactionID = TransactionIDGenerator.generateTransactionID();
        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        ServiceLogger.LOGGER.info("movie/search:: Received request to search movie for email: "+email);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();
        ServiceLogger.LOGGER.info("movie/search:: session is not null\n");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("movie/search:: new sessionID: "+newSessionID);

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPMovieSearch());

        clientRequest.setHTTPMethod(ResultCode.GET_METHOD);
        clientRequest.setQueryParams(queryParams);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();
        ServiceLogger.LOGGER.info("get/{movieid}:: Received request to search for movieID: "+movieid);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        ServiceLogger.LOGGER.info("get/{movieid}:: session not null\n");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("get/{movieid}:: new sessionID: "+newSessionID);

        ServiceLogger.LOGGER.info("Endpoint path: "+ GatewayService.getMovieConfigs().getEPMovieGet()+"/"+movieid);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPMovieGet()+"/"+movieid);

        clientRequest.setEndpoint("/get/"+movieid);

        clientRequest.setHTTPMethod(ResultCode.GET_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers, String jsonText) {
        MovieAddRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();
        ServiceLogger.LOGGER.info("movie/add:: Received request to add movie for email: "+email);

//        if (sessionID == null)
//            return Response.status(Status.BAD_REQUEST).
//                    header("email", email).
//                    header("transactionID", transactionID).
//                    entity(new VerifySessionResponseModel(-17)).build();

        ServiceLogger.LOGGER.info("movie/add:: session is not null\n");
        try{
            requestModel = (MovieAddRequestModel) ModelValidator.verifyModel(jsonText, MovieAddRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, MovieAddResponseModel.class);
        }

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("movie/add:: new sessionID: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPMovieAdd());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("delete/{movieid}:: Received request to delete movieID: "+movieid);

//        if (sessionID == null)
//            return Response.status(Status.BAD_REQUEST).
//                    header("email", email).
//                    header("transactionID", transactionID).
//                    entity(new VerifySessionResponseModel(-17)).build();
//        ServiceLogger.LOGGER.info("delete/{movieid}:: session not null\n");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();

        ServiceLogger.LOGGER.info("delete/{movieid}:: new sessionID"+newSessionID);
        //ServiceLogger.LOGGER.info("Endpoint path: "+ GatewayService.getMovieConfigs().getEPMovieSearch()+"/"+movieid);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPMovieDelete());

        clientRequest.setHTTPMethod(ResultCode.DELETE_METHOD);

        clientRequest.setEndpoint("/delete/"+movieid);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("movie/genre:: Received request to retrieve all genre for email: "+email);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        ServiceLogger.LOGGER.info("movie/genre:: session is not null\n");
        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("movie/genre:: new sessionID: "+newSessionID);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPGenreGet());

        clientRequest.setHTTPMethod(ResultCode.GET_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers, String jsonText) {
        GenreRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("genre/add:: Received request to add new genre for email: "+email);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        ServiceLogger.LOGGER.info("genre/add:: session is not null\n");
        try{
            requestModel = (GenreRequestModel) ModelValidator.verifyModel(jsonText, GenreRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, MovieResponseModel.class);
        }

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("genre/add:: new sessionID: "+newSessionID);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPGenreAdd());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers, @PathParam("movieid") String movieid) {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("genre/{movieid}:: received request to retrieve genre of movieID "+movieid);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();
        ServiceLogger.LOGGER.info("genre/{movieid}:: session is not null\n");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);

        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("genre/{movieid}:: new sessionID: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPGenreMovie()+movieid);

        clientRequest.setHTTPMethod(ResultCode.GET_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo) {
        ServiceLogger.LOGGER.info("MovieEP:: Received request to search star.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPStarSearch());

        clientRequest.setHTTPMethod(ResultCode.GET_METHOD);
        clientRequest.setQueryParams(queryParams);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("star/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers, @PathParam("id") String id) {
        ServiceLogger.LOGGER.info("MovieEP:: Received request to search for starID");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();

        ServiceLogger.LOGGER.info("Star/{id}:: session is not null");

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("Star/{id}:: new sessionID: "+newSessionID);

        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, null,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPStarGet()+id);

        clientRequest.setHTTPMethod(ResultCode.GET_METHOD);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("MovieEP:: Received request to add new star");
        StarAddRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();
        ServiceLogger.LOGGER.info("Star/add:: session is not null");
        try{
            requestModel = (StarAddRequestModel) ModelValidator.verifyModel(jsonText, StarAddRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, StarAddResponseModel.class);
        }

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("Star/add:: new sessionID: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPStarAdd());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);
        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers, String jsonText) {
        StarMovieRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();
        ServiceLogger.LOGGER.info("MovieEP:: Received request to add star in movie for email: "+email);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();
        ServiceLogger.LOGGER.info("Star/starsin:: session is not null");
        try{
            requestModel = (StarMovieRequestModel) ModelValidator.verifyModel(jsonText, StarMovieRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, StarMovieResponseModel.class);
        }

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("Star/starsin:: new sessionID: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPStarIn());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);

        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers, String jsonText) {
        RatingRequestModel requestModel;

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = TransactionIDGenerator.generateTransactionID();

        ServiceLogger.LOGGER.info("MovieEP:: Received request to rate movie for email: "+email);

        if (sessionID == null)
            return Response.status(Status.BAD_REQUEST).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    entity(new VerifySessionResponseModel(-17)).build();
        ServiceLogger.LOGGER.info("rating:: session is not null");
        try{
            requestModel = (RatingRequestModel) ModelValidator.verifyModel(jsonText, RatingRequestModel.class);
        }catch (ModelValidationException e){
            return ModelValidator.returnInvalidRequest(e, RatingResponseModel.class);
        }

        VerifySessionResponseModel rm = Session.verify(email, sessionID);
        if (rm.getResultCode() != ResultCodes.SESSION_ACTIVE)
            return Response.status(Status.OK).
                    header("Access-Control-Allow-Origin", "*").
                    header("Access-Control-Expose-Headers", "*").
                    header("email", email).
                    header("transactionID", transactionID).
                    header("sessionID", sessionID).
                    entity(rm).build();

        String newSessionID = rm.getSessionID();
        ServiceLogger.LOGGER.info("movie/rating:: new sessionID: "+newSessionID);
        ClientRequest clientRequest = new ClientRequest(email,
                newSessionID, transactionID, requestModel,
                GatewayService.getMovieConfigs().getMoviesUri(),
                GatewayService.getMovieConfigs().getEPRating());

        clientRequest.setHTTPMethod(ResultCode.POST_METHOD);
        GatewayService.getThreadPool().add(clientRequest);

        NoContentResponseModel responseModel = new NoContentResponseModel(GatewayService.getGatewayConfigs().getRequestDelay(), transactionID);

        return Response.status(Status.NO_CONTENT).
                header("Access-Control-Allow-Origin", "*").
                header("Access-Control-Expose-Headers", "*").
                header("email", email).
                header("sessionID", newSessionID).
                header("transactionID", transactionID).
                entity(responseModel).build();
    }
}
