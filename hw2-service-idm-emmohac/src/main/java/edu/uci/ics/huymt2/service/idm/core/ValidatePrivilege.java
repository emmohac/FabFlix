package edu.uci.ics.huymt2.service.idm.core;

import edu.uci.ics.huymt2.service.idm.IDMService;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.VerifyPrivilegeRequestModel;
import edu.uci.ics.huymt2.service.idm.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidatePrivilege {
    public static VerifyPrivilegeResponseModel validate(VerifyPrivilegeRequestModel requestModel){
        String email = requestModel.getEmail();
        int plevel = requestModel.getPlevel();
        ServiceLogger.LOGGER.info("Validating email: "+email);

        // Checking for validity of email and privilege level before processing any further
        if (email.length() > 50){
            ServiceLogger.LOGGER.info("ValidatePrivilege: email is too long.");
            return new VerifyPrivilegeResponseModel(-10);
        }

        if (!Validate.isEmailFormatted(email)){
            ServiceLogger.LOGGER.info("ValidatePrivilege: email is not formatted.");
            return new VerifyPrivilegeResponseModel(-11);
        }

        if (plevel > 5 || plevel < 1){
            ServiceLogger.LOGGER.info("ValidatePrivilege: plevel is out of range.");
            return new VerifyPrivilegeResponseModel(ResultCode.PLEVEL_INVALID);
        }

        try{
            String query = "SELECT plevel FROM users WHERE email = ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1,email);
            ServiceLogger.LOGGER.info("Trying query: "+ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query successfully.");

            // Checking if there exists a user with the given privilege level
            if (!rs.next()){
                ServiceLogger.LOGGER.info("Nothing in DB.");
                return new VerifyPrivilegeResponseModel(ResultCode.USER_NOT_FOUND);
            }

            int dbPlevel = rs.getInt("plevel");
            if (dbPlevel > plevel) {
                ServiceLogger.LOGGER.info("not matched here");
                return new VerifyPrivilegeResponseModel(ResultCode.INSUFFICIENT_PRIVILEGE);
            }
        }catch(SQLException e){
            ServiceLogger.LOGGER.info("ValidatePrivilege: cannot query");
            e.printStackTrace();
        }

        // User is found and the privilege level is sufficient to access the endpoint
        return new VerifyPrivilegeResponseModel(ResultCode.SUFFICIENT_PRIVILEGE);
    }
}
