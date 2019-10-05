package edu.uci.ics.huymt2.service.billing.logicalhandler;

import edu.uci.ics.huymt2.service.billing.BillingService;
import edu.uci.ics.huymt2.service.billing.core.CreditCardInfo;
import edu.uci.ics.huymt2.service.billing.core.HelpMe;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.models.creditcard.CreditCardRequestModel;
import edu.uci.ics.huymt2.service.billing.models.creditcard.CreditCardResponseModel;
import edu.uci.ics.huymt2.service.billing.models.creditcard.DeleteCCardRequestModel;
import edu.uci.ics.huymt2.service.billing.models.creditcard.RetrieveCCardResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class CreditCard {
    public static CreditCardResponseModel insertCCardToDBFrom(CreditCardRequestModel requestModel){
        String id = requestModel.getId();
        Date expiration = requestModel.getExpiration();

        ServiceLogger.LOGGER.info("Received insert request for id: "+id);

        int idCode = HelpMe.verifyCreditCardID(id);
        if (idCode != 0)
            return new CreditCardResponseModel(idCode);

        ServiceLogger.LOGGER.info("CreditCard::card ID is valid.");

        if (!HelpMe.verifyCreditCardExpiration(expiration))
            return new CreditCardResponseModel(ResultCode.INVALID_EXPIRATION);

        ServiceLogger.LOGGER.info("CreditCard::card expiration is valid.");

        try{
            String query = "INSERT INTO creditcards(id, firstName, lastName, expiration) VALUES(?,?,?,?);";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, requestModel.getFirstName());
            ps.setString(3, requestModel.getLastName());
            ps.setDate(4, expiration);
            ps.execute();
            return new CreditCardResponseModel(ResultCode.SUCCESSFULLY_INSERTED_CREDITCARD);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("CreditCard::failure to insert credit card.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
            return new CreditCardResponseModel(ResultCode.DUPLICATE_CREDITCARD_INSERT);
        }
    }

    public static CreditCardResponseModel updateCCardToDBFrom(CreditCardRequestModel requestModel){
        String id = requestModel.getId();
        Date newExpiration = requestModel.getExpiration();

        ServiceLogger.LOGGER.info("CreditCard:: update request for id: "+id);

        int idCode = HelpMe.verifyCreditCardID(id);
        if (idCode != 0)
            return new CreditCardResponseModel(idCode);

        ServiceLogger.LOGGER.info("CreditCard::card ID is valid.");

        if (!HelpMe.verifyCreditCardExpiration(newExpiration))
            return new CreditCardResponseModel(ResultCode.INVALID_EXPIRATION);

        ServiceLogger.LOGGER.info("CreditCard::card expiration is valid.");

        try{
            String query = "UPDATE creditcards SET firstName = ?, lastName = ?, expiration = ? WHERE id = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getFirstName());
            ps.setString(2, requestModel.getLastName());
            ps.setDate(3, newExpiration);
            ps.setString(4, id);
            if (ps.executeUpdate() != 0)
                return new CreditCardResponseModel(ResultCode.SUCCESSFULLY_UPDATED_CREDITCARD);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("CreditCard::failure to update credit card");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new CreditCardResponseModel(ResultCode.CREDITCARD_NOT_EXISTED);
    }

    public static CreditCardResponseModel deleteCCardFromDB(DeleteCCardRequestModel requestModel){
        String id = requestModel.getId();

        ServiceLogger.LOGGER.info("CreditCard:: update request for id: "+id);

        int idCode = HelpMe.verifyCreditCardID(id);
        if (idCode != 0)
            return new CreditCardResponseModel(idCode);

        ServiceLogger.LOGGER.info("CreditCard:: credit card ID is valid.");

        try{
            String queryCheck = "SELECT * FROM creditcards WHERE id = ?;";
            PreparedStatement psCheck = BillingService.getCon().prepareStatement(queryCheck);
            psCheck.setString(1, id);
            ResultSet rs = psCheck.executeQuery();
            if (!rs.next())
                return new CreditCardResponseModel(ResultCode.CREDITCARD_NOT_EXISTED);

            String query = "DELETE FROM creditcards WHERE id = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ps.execute();
            return new CreditCardResponseModel(ResultCode.SUCCESSFULLY_DELETED_CREDITCARD);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("CreditCard:: failure to delete credit card.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new CreditCardResponseModel(ResultCode.CREDITCARD_NOT_EXISTED);
    }

    public static RetrieveCCardResponseModel retrieveCCardFromDB(DeleteCCardRequestModel requestModel){
        String id = requestModel.getId();

        ServiceLogger.LOGGER.info("CreditCard:: retrieve request for id: "+id);

        int idCode = HelpMe.verifyCreditCardID(id);
        if (idCode != 0) {
            return new RetrieveCCardResponseModel(idCode);
        }
        ServiceLogger.LOGGER.info("CreditCard:: credit card ID is valid");

        try{
            String query = "SELECT id, firstName, lastName, expiration FROM creditcards WHERE id = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                CreditCardInfo cc = new CreditCardInfo(rs.getString("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getDate("expiration"));
                RetrieveCCardResponseModel rm = new RetrieveCCardResponseModel(ResultCode.SUCCESSFULLY_RETRIEVED_CREDITCARD);
                rm.setCreditcard(cc);
                return rm;
            }
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("CreditCard:: failure to retrieve credit card");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new RetrieveCCardResponseModel(ResultCode.CREDITCARD_NOT_EXISTED);
    }
}
