package edu.uci.ics.huymt2.service.movies.configs;

import edu.uci.ics.huymt2.service.movies.logger.ServiceLogger;

public class IDMConfigs {
    private final int MIN_SERVICE_PORT = 1024;
    private final int MAX_SERVICE_PORT = 65535;

    private String scheme;
    private String hostName;
    private int port;
    private String path;

    private String privilege;

    public IDMConfigs() {}

    public IDMConfigs(ConfigsModel cm) throws NullPointerException {
        if (cm == null)
            throw new NullPointerException("Unable to create IDMConfigs from ConfigsModel");
        else{
            scheme = cm.getIdmConfig().get("scheme");
            if (scheme == null)
                ServiceLogger.LOGGER.info("Scheme not found in idmConfigs");
            else
                ServiceLogger.LOGGER.info("Scheme: "+scheme);

            hostName = cm.getIdmConfig().get("hostName");
            if (hostName == null)
                ServiceLogger.LOGGER.info("hostName not found in idmConfigs");
            else
                ServiceLogger.LOGGER.info("hostName: "+hostName);

            port = Integer.parseInt(cm.getIdmConfig().get("port"));
            if (port == 0)
                ServiceLogger.LOGGER.info("port not found in idmConfigs");
            else if (port < MIN_SERVICE_PORT || port > MAX_SERVICE_PORT)
                ServiceLogger.LOGGER.info("port is not valid");
            else
                ServiceLogger.LOGGER.info("port: "+port);

            path = cm.getIdmConfig().get("path");
            if (path == null)
                ServiceLogger.LOGGER.info("path not found in idmConfigs");
            else
                ServiceLogger.LOGGER.info("path: "+path);

            privilege = cm.getIdmEndpoints().get("privilege");
            if (privilege == null)
                ServiceLogger.LOGGER.info("privilege not found in idmConfigs");
            else
                ServiceLogger.LOGGER.info("privilege: "+privilege);
        }
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public void currentIDMConfigs() {
        ServiceLogger.LOGGER.config("IDMConfig Scheme: " + scheme);
        ServiceLogger.LOGGER.config("IDMConfig Hostname: " + hostName);
        ServiceLogger.LOGGER.config("IDMConfig Port: " + port);
        ServiceLogger.LOGGER.config("IDMConfig Path: " + path);
        ServiceLogger.LOGGER.config("IDMConfig Privilege: "+privilege);
    }

    public String getIdmUri() {
        return scheme + hostName + ":" + port + path;
    }
    public String getPrivilegePath() {
        return privilege;
    }
}
