package edu.uci.ics.huymt2.service.api_gateway.resources;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.Response.Status;

@Path("report")
public class ReportPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestReport(@Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Receive request to print report");
        String transactionID = headers.getHeaderString("transactionID");
        String sessionID = headers.getHeaderString("sessionID");
        ServiceLogger.LOGGER.info("ReportPage:: finding report for transactionID: "+transactionID);
        Connection con = GatewayService.getConPool().requestCon();

        try{
            String query = "SELECT * FROM responses WHERE transactionid = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, transactionID);
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet resultSet = ps.executeQuery();
            ServiceLogger.LOGGER.info("Finished query.");
            ResultSet rs = resultSet;

            if (rs.next()){
                ServiceLogger.LOGGER.info("Successfully retrieved report.");
                String email = rs.getString("email");
                String dbSessionID = rs.getString("sessionID");

//                String queryDelete = "DELETE FROM responses WHERE transactionid = ?;";
//                PreparedStatement psDelete = con.prepareStatement(queryDelete);
//                psDelete.setString(1, transactionID);
//                psDelete.execute();
//                ServiceLogger.LOGGER.info("Successfully deleted transactionID");

                GatewayService.getConPool().releaseCon(con);
                return Response.status(Status.fromStatusCode(rs.getInt("httpstatus")))
                        .header("email", email)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("sessionID", dbSessionID)
                        .header("transactionID", transactionID)
                        .entity(rs.getString("response")).build();
            }
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("SQL Exception");
        }
        GatewayService.getConPool().releaseCon(con);
        return Response.status(Status.NO_CONTENT)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .build();
    }
}
