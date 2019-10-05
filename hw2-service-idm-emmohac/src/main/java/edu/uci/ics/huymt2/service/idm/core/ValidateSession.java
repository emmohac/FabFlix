package edu.uci.ics.huymt2.service.idm.core;


import edu.uci.ics.huymt2.service.idm.IDMService;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.SessionModel;
import edu.uci.ics.huymt2.service.idm.models.VerifySessionRequestModel;
import edu.uci.ics.huymt2.service.idm.models.VerifySessionResponseModel;
import edu.uci.ics.huymt2.service.idm.security.Session;
import edu.uci.ics.huymt2.service.idm.security.Token;

import java.sql.*;

public class ValidateSession {
    public static VerifySessionResponseModel validate(VerifySessionRequestModel sessionRequestModel){
        String email = sessionRequestModel.getEmail();
        String ssID = sessionRequestModel.getSessionID();

        // Checking email and password requirement before going any further
        if (email == null || email.length() > 50 || email.length() == 0){
            ServiceLogger.LOGGER.info("ValidateSession: email has invalid length");
            return new VerifySessionResponseModel(ResultCode.EMAIL_INVALID_LENGTH);
        }

        if (!Validate.isEmailFormatted(email)){
            ServiceLogger.LOGGER.info("ValidateSession: email format is not correct.");
            return new VerifySessionResponseModel(ResultCode.EMAIL_INVALID_FORMAT);
        }

        if (ssID == null)
            return new VerifySessionResponseModel(-17);

        if (ssID.length() != 128){
            ServiceLogger.LOGGER.info("ValidateSession: sessionID has invalid length");
            return new VerifySessionResponseModel(ResultCode.SESSION_INVALID_LENGTH);
        }

        try {
            String query = "SELECT * FROM sessions WHERE email = ? AND sessionID = ?";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("ValidateSession: trying to query "+ps.toString());
            ServiceLogger.LOGGER.info("User email: "+email);
            ps.setString(1, email);
            ps.setString(2, ssID);
            ResultSet rs = ps.executeQuery();

            ServiceLogger.LOGGER.info("ValidateSession: query successfully.");

            // Checking if there exist a user as given in the response model
            if (!rs.next()){
                ServiceLogger.LOGGER.info("Found no one.");
                return new VerifySessionResponseModel(14);
            }

            String querySession = "SELECT sessionID, status FROM sessions WHERE sessionID = ?;";
            PreparedStatement psSession = IDMService.getCon().prepareStatement(querySession);
            psSession.setString(1, ssID);
            ResultSet rsSession = psSession.executeQuery();

            // Checking for validation of the given session
            if (!rsSession.next()){
                ServiceLogger.LOGGER.info("ValidateSession: sessionIDs not matched.");
                return new VerifySessionResponseModel(ResultCode.SESSION_NOT_FOUND);
            }

            int dbStatus = rsSession.getInt("status");
            ServiceLogger.LOGGER.info("ValidateSession: dbStatus: "+dbStatus);

            // If the session is not active, there is no point in going any further than that.
            // Only check the current status of the session in the table
            if (dbStatus != Session.ACTIVE){
                if (dbStatus == Session.CLOSED)
                    return new VerifySessionResponseModel(ResultCode.SESSION_CLOSED);
                else if (dbStatus == Session.EXPIRED) {
                    ServiceLogger.LOGGER.info("ValidateSession:: Tony Handsome.");
                    return new VerifySessionResponseModel(ResultCode.SESSION_EXPIRED);
                }
                else
                    return new VerifySessionResponseModel(ResultCode.SESSION_REVOKED);
            }

            // If the session is active, the session needs to be verified if it is still within the time range
            // that allow the user to use the service, or already passed the allowed time or if it is about the pass
            // the expired time but the user is still active

            rs.first();             // Move back the cursor of the table to the first row in the table
            Timestamp dbLastUsed = rs.getTimestamp("lastUsed");     // Getting the timestamp that the user last used
            Timestamp dbExprTime = rs.getTimestamp("exprTime");     // Getting the expired time of the session
            Timestamp currentTime = new Timestamp(System.currentTimeMillis()); // Getting the current time to compare
            // Getting the timeout of the session. If the user has been idle more than timeout allowed, then the user
            // will be forced to log in again in order to gain a new valid session
            Timestamp timeoutTime = new Timestamp(dbLastUsed.getTime() + Session.SESSION_TIMEOUT);

            // If the current time is even before the timeout, then it is valid and no further action required
            if (currentTime.before(timeoutTime)){
                String queryUpdate = "UPDATE sessions SET lastUsed = ? WHERE sessionID = ?;";
                PreparedStatement psUpdate = IDMService.getCon().prepareStatement(queryUpdate);
                psUpdate.setTimestamp(1, currentTime);
                psUpdate.setString(2, ssID);
                psUpdate.executeUpdate();
                ServiceLogger.LOGGER.info("ValidateSession:: lastUsed is within the timeout range.");
                ServiceLogger.LOGGER.info("ValidateSession:: updated lastUsed in sessions successfully.");
                VerifySessionResponseModel responseModel = new VerifySessionResponseModel(ResultCode.SESSION_ACTIVE);
                responseModel.setSessionID(ssID);
                return responseModel;
            }

            // If the current time has passed the expired time of the session, then the session is no longer valid
            // and the session will be marked expired. The user will have to log in again to receive a valid session.
            if (currentTime.after(dbExprTime)){
                ServiceLogger.LOGGER.info("ValidateSession:: email "+email);
                ServiceLogger.LOGGER.info("ValidateSession:: sessionID expired "+ssID);
                String queryUpdate = "UPDATE sessions SET lastUsed = ?, status = ? WHERE sessionID = ?;";
                PreparedStatement psUpdate = IDMService.getCon().prepareStatement(queryUpdate);
                psUpdate.setTimestamp(1, currentTime);
                psUpdate.setInt(2, Session.EXPIRED);
                psUpdate.setString(3, ssID);
                psUpdate.executeUpdate();
                ServiceLogger.LOGGER.info("ValidateSession:: successfully set expired to session above.");
                return new VerifySessionResponseModel(ResultCode.SESSION_EXPIRED);
            }

            // If the user has been idle longer than the timeout allowed, then the user will be forced to log in again
            // to receive a valid session. The current session will be set to revoked
            if (currentTime.after(timeoutTime)){
                ServiceLogger.LOGGER.info("ValidateSession:: user passed session timeout => REVOKED");
                String queryUpdate = "UPDATE sessions SET status = ? WHERE sessionID = ?;";
                PreparedStatement psUpdate = IDMService.getCon().prepareStatement(queryUpdate);
                psUpdate.setInt(1, Session.REVOKED);
                psUpdate.setString(2, ssID);
                psUpdate.executeUpdate();
                return new VerifySessionResponseModel(ResultCode.SESSION_REVOKED);
            }

            // If the session is going to expired and the user is still active then the user will receive another
            // valid session without logging in again. The current session will be marked as revoked. The user can still
            // uses the service without interruption
            if (Math.abs(dbLastUsed.getTime() - currentTime.getTime()) < dbExprTime.getTime()){
                String queryUpdate = "UPDATE sessions SET status = ? WHERE sessionID = ?;";
                PreparedStatement psUpdate = IDMService.getCon().prepareStatement(queryUpdate);
                psUpdate.setInt(1, Session.REVOKED);
                psUpdate.setString(2, ssID);
                psUpdate.executeUpdate();
                ServiceLogger.LOGGER.info("ValidateSession :: queryUpdate successfully.");

                Session session = Session.createSession(email);
                SessionModel sessionModel = new SessionModel(session.getEmail(), session.getSessionID().toString());

                String queryInsert = "INSERT INTO sessions(email, sessionID, status, timeCreated, lastUsed, exprTime) VALUES (?,?,?,?,?,?);";
                PreparedStatement psInsert = IDMService.getCon().prepareStatement(queryInsert);

                psInsert.setString(1,email);
                psInsert.setString(2, session.getSessionID().toString());
                psInsert.setInt(3,Session.ACTIVE);
                psInsert.setTimestamp(4, session.getTimeCreated());
                psInsert.setTimestamp(5, session.getLastUsed());
                psInsert.setTimestamp(6, session.getExprTime());
                psInsert.execute();
                ServiceLogger.LOGGER.info("ValidateSession:: queryUpdate successfully.");
                VerifySessionResponseModel responseModel = new VerifySessionResponseModel(ResultCode.SESSION_ACTIVE);
                responseModel.setSessionID(sessionModel.getSessionID());
                return responseModel;
            }
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("ValidateSession: cannot query");
            e.printStackTrace();
        }
        return new VerifySessionResponseModel(-1);
    }
}
