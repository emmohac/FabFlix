package edu.uci.ics.huymt2.service.idm.core;

import edu.uci.ics.huymt2.service.idm.IDMService;
import edu.uci.ics.huymt2.service.idm.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.huymt2.service.idm.models.RegisterResponseModel;
import edu.uci.ics.huymt2.service.idm.security.Crypto;
import edu.uci.ics.huymt2.service.idm.security.Session;
import org.apache.commons.codec.binary.Hex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ValidateRegister {
    public static RegisterResponseModel validate(RegisterRequestModel registerRequest){
        String email = registerRequest.getEmail();
        char[] password = registerRequest.getPassword();
        ServiceLogger.LOGGER.info("Verifying email: "+email);

        // Checking for email and password requirement before going any further
        if (password == null || password.length > 128 || password.length == 0){
            ServiceLogger.LOGGER.info("Password has invalid length.");
            return new RegisterResponseModel(ResultCode.PASSWORD_INVALID_LENGTH);
        }

        if (!Validate.isEmailFormatted(email)){
            ServiceLogger.LOGGER.info("Email format is not valid.");
            return new RegisterResponseModel(-11);
        }

        if (email.length() > 50){
            ServiceLogger.LOGGER.info("Email length is not valid. Must be < 50.");
            return new RegisterResponseModel(-10);
        }

        if (password.length < 7 || password.length > 16){
            ServiceLogger.LOGGER.info("Password length is too short or too long.");
            return new RegisterResponseModel(12);
        }

        if (!Validate.isPasswordFormat(password)){
            ServiceLogger.LOGGER.info("Password requirement is not valid");
            return new RegisterResponseModel(13);
        }

        try{
            String query = "SELECT email FROM users WHERE email = ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1,email);
            ServiceLogger.LOGGER.info("Trying query: "+ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query successfully.");

            // Checking for existing user
            if (rs.next()){
                ServiceLogger.LOGGER.info("Email already existed.");
                return new RegisterResponseModel(ResultCode.EMAIL_ALREADY_USED);
            }

            String insertQuery = "INSERT INTO users(email, plevel, salt, pword, status) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement psInsert = IDMService.getCon().prepareStatement(insertQuery);

            // Hashing password to improve security. No one would know the password.
            ServiceLogger.LOGGER.info("Hashing password.");
            byte[] salt = Crypto.genSalt();
            byte[] hashedPassword = Crypto.hashPassword(password, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);

            // Getting the hashed password
            String pword = Validate.getHashedPass(hashedPassword);
            String shortSalt = Hex.encodeHexString(salt);
            ServiceLogger.LOGGER.info("Hashed successfully.");
            psInsert.setString(1, email);
            psInsert.setInt(2, 5);
            psInsert.setString(3, shortSalt);
            psInsert.setString(4,pword);
            // Setting the session active
            psInsert.setInt(5, Session.ACTIVE);
            psInsert.execute();

            // After inserting all data into the table, fill the given password with 0 to improve security and
            // avoid password being stolen
            Arrays.fill(password, '0');
            ServiceLogger.LOGGER.info("Successfully registered email: "+email);
        }catch(SQLException e){
            ServiceLogger.LOGGER.info("Error in ValidateRegister");
            e.printStackTrace();
        }

        // Email and password meet the requirement.
        return new RegisterResponseModel(ResultCode.SUCCESSFULLY_REGISTERED);
    }
}
