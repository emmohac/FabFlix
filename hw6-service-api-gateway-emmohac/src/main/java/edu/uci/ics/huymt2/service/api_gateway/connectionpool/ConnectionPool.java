package edu.uci.ics.huymt2.service.api_gateway.connectionpool;

import edu.uci.ics.huymt2.service.api_gateway.GatewayService;
import edu.uci.ics.huymt2.service.api_gateway.logger.ServiceLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {
    LinkedList<Connection> connections;
    String driver;
    String url;
    String username;
    String password;

    public ConnectionPool(int numCons, String driver, String url, String username, String password) {
        ServiceLogger.LOGGER.info("Initializing connection pool...");
        connections = new LinkedList<>();
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;

        try{
            Class.forName(driver);
            for (int i = 0; i < numCons; ++i){
                ServiceLogger.LOGGER.info("Creating connnection: "+(i+1));
                ServiceLogger.LOGGER.info("URL: "+url);
                ServiceLogger.LOGGER.info("driver: "+driver);
                ServiceLogger.LOGGER.info("username: "+username);
                connections.add(createConnection());
            }
        }catch (ClassNotFoundException e){
            ServiceLogger.LOGGER.info("Unable to load driver into memory.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
    }

    public synchronized Connection requestCon() {
            if (connections.isEmpty())
                return createConnection();
            else {
                Connection connection = connections.getFirst();
                connections.removeFirst();
                return connection;
            }
    }

    public synchronized void releaseCon(Connection con) {
        connections.add(con);
    }

    private Connection createConnection() {
        Connection con = null;
        try{
            ServiceLogger.LOGGER.info("Creating connection to: "+url);
            con = DriverManager.getConnection(url, username, password);
            ServiceLogger.LOGGER.info("Successfully created connection.");
           // connections.addLast(con);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("ConnectionPool:: failure to create connection.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return con;
    }
}
