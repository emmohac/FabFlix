package edu.uci.ics.huymt2.service.idm.core;

import edu.uci.ics.huymt2.service.idm.IDMService;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.LoginRequestModel;
import edu.uci.ics.huymt2.service.idm.models.LoginResponseModel;
import edu.uci.ics.huymt2.service.idm.models.SessionModel;
import edu.uci.ics.huymt2.service.idm.security.Crypto;
import edu.uci.ics.huymt2.service.idm.security.Session;
import edu.uci.ics.huymt2.service.idm.security.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ValidateLogin {
    public static LoginResponseModel validate(LoginRequestModel loginRequest){
        String email = loginRequest.getEmail();
        char[] password = loginRequest.getPassword();

        // Checking for password requirement
        if (password == null || password.length == 0 || password.length > 128){
            ServiceLogger.LOGGER.info("Password has invalid length.");
            return new LoginResponseModel(ResultCode.PASSWORD_INVALID_LENGTH);
        }

        if (email.length() > 50){
            ServiceLogger.LOGGER.info("Email is too long.");
            return new LoginResponseModel(ResultCode.EMAIL_INVALID_LENGTH);
        }

        if (!Validate.isEmailFormatted(email)){
            ServiceLogger.LOGGER.info("Email format is invalid.");
            return new LoginResponseModel(ResultCode.EMAIL_INVALID_FORMAT);
        }

        // Checking for existing user in database
        try{
            String query = "SELECT salt, pword FROM users WHERE email = ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query: "+ps.toString());
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query successfully.");

            // If there is not user matched the response model, then return user not found
            if (!rs.next()){
                ServiceLogger.LOGGER.info("User not found in ValidateLogin");
                return new LoginResponseModel(ResultCode.USER_NOT_FOUND);
            }

            ServiceLogger.LOGGER.info("Checking password");
            String dbSalt = rs.getString("salt");

            // Hashing password then compare to the current hashed password in the database
            byte[] hashedPassword = Crypto.hashPassword(password, Token.convert(dbSalt), Crypto.ITERATIONS, Crypto.KEY_LENGTH);
            String inputPassword = Validate.getHashedPass(hashedPassword);
            ServiceLogger.LOGGER.info("hashed password successfully");
            String dbPassword = rs.getString("pword");
            ServiceLogger.LOGGER.info("dbPassword: "+dbPassword);
            ServiceLogger.LOGGER.info("Comparing password");

            if (inputPassword.compareTo(dbPassword) != 0){
                ServiceLogger.LOGGER.info("Password does not match.");
                return new LoginResponseModel(ResultCode.PASSWORD_NOT_MATCH);
            }

            // Found user. Giving user the session to verify at each other endpoints
            ServiceLogger.LOGGER.info("Password matched. Updating session for existing record.");
            String sessionQuery = "UPDATE sessions SET status = ? WHERE email = ? AND status = ?;";
            PreparedStatement sessionps = IDMService.getCon().prepareStatement(sessionQuery);
            sessionps.setInt(1, Session.REVOKED); // Setting the current session to be revoked and giving the user
            sessionps.setString(2, email);        // user a brand new session in case the current session is still
            sessionps.setInt(3,Session.ACTIVE);   // active but the user tries to log in again
            sessionps.executeUpdate();

            // Generating new session
            Session session = Session.createSession(email);
            SessionModel sessionModel = new SessionModel(session.getEmail(), session.getSessionID().toString());

            // Inserting an active valid session of the user into the table
            String queryInsert = "INSERT INTO sessions(email, sessionID, status, timeCreated, lastUsed, exprTime) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement psInsert = IDMService.getCon().prepareStatement(queryInsert);
            psInsert.setString(1, email);
            psInsert.setString(2, session.getSessionID().toString());
            psInsert.setInt(3, 1);
            psInsert.setTimestamp(4, session.getTimeCreated());
            psInsert.setTimestamp(5, session.getLastUsed());
            psInsert.setTimestamp(6, session.getExprTime());
            psInsert.execute();

            ServiceLogger.LOGGER.info("Session created. Trying query: "+psInsert.toString());
            LoginResponseModel rm = new LoginResponseModel(ResultCode.SUCCESSFULLY_LOGIN);
            rm.setSessionID(sessionModel.getSessionID());
            return rm;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Query fails in ValidateLogin.");
            e.printStackTrace();
        }
        return new LoginResponseModel(-1);
    }
}
