package edu.uci.ics.huymt2.service.api_gateway.threadpool;


import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResultCode;
import org.glassfish.jersey.jackson.JacksonFeature;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }

    public void process() {
        ClientRequest clientRequest = threadPool.remove();
        if (clientRequest == null)
            return;

        String email = clientRequest.getEmail();
        String endPoint = clientRequest.getEndpoint();
        ServiceLogger.LOGGER.info("Worker:: Email: "+email);
        RequestModel rm = clientRequest.getRequest();

        String URI = clientRequest.getURI();
        String sessionID = clientRequest.getSessionID();
        String transactionID = clientRequest.getTransactionID();

        int HTTPMethod = clientRequest.getHTTPMethod();
        MultivaluedMap<String, String> queryParams = clientRequest.getQueryParams();

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        //WebTarget webTarget = client.target(URI);

        Response response;

        if (HTTPMethod == ResultCode.POST_METHOD) {
            WebTarget webTarget = client.target(URI).path(endPoint);
            Invocation.Builder invocation = webTarget.request(MediaType.APPLICATION_JSON).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID);
            response = invocation.post(Entity.entity(rm, MediaType.APPLICATION_JSON));
        }
        else if (HTTPMethod == ResultCode.GET_METHOD) {
            WebTarget webTarget = client.target(URI).path(endPoint);
            ServiceLogger.LOGGER.info("GET_METHOD URL: "+webTarget.toString());
            if (queryParams != null)
                for (Map.Entry<String, List<String>> entry : queryParams.entrySet()){
                    webTarget = webTarget.queryParam(entry.getKey(), entry.getValue().get(0));
                }
            else
                webTarget = client.target(URI+endPoint);

            Invocation.Builder invocation = webTarget.request(MediaType.APPLICATION_JSON).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID);
            Invocation invo = invocation.buildGet();
            response = invo.invoke();
        }
        else{//delete endpoint
            WebTarget webTarget = client.target(URI+endPoint);
            Invocation.Builder invo = webTarget.request(MediaType.APPLICATION_JSON).
                    header("email", email).
                    header("sessionID", sessionID).
                    header("transactionID", transactionID);
            Invocation invocation = invo.buildDelete();
            response = invocation.invoke();
        }

        Connection myConnection = GatewayService.getConPool().requestCon();

        ServiceLogger.LOGGER.info("Connection created.");

        try{
            String query = "INSERT INTO responses(transactionid, email, sessionid, response, httpstatus) VALUES(?,?,?,?,?);";
            PreparedStatement ps = myConnection.prepareStatement(query);
            ps.setString(1, transactionID);
            ps.setString(2, email);
            ps.setString(3, sessionID);
            ps.setString(4, response.readEntity(String.class));
            ps.setInt(5, response.getStatus());
            ps.execute();
            ServiceLogger.LOGGER.info("Worker:: Successfully inserted response into DB.");
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Worker:: failure to insert response into DB");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        GatewayService.getConPool().releaseCon(myConnection);
        ServiceLogger.LOGGER.info("Worker:: Connection released.");
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }
}
