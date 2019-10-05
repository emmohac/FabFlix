package edu.uci.ics.huymt2.service.billing.logicalhandler;

import edu.uci.ics.huymt2.service.billing.BillingService;
import edu.uci.ics.huymt2.service.billing.core.HelpMe;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.models.customer.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    public static CustomerResponseModel insertCustomerFrom(CustomerRequestModel requestModel){
        String ccId = requestModel.getCcId();

        ServiceLogger.LOGGER.info("Customer:: receive insert customer with ccId: "+ccId);
        int idCode = HelpMe.verifyCreditCardID(ccId);
        if (idCode != 0)
            return new CustomerResponseModel(idCode);

        ServiceLogger.LOGGER.info("Customer:: credit card ID is valid.");

        try{
            String queryCard = "SELECT id FROM creditcards WHERE id = ?;";
            PreparedStatement psCard = BillingService.getCon().prepareStatement(queryCard);
            psCard.setString(1, ccId);
            ResultSet rsCard = psCard.executeQuery();

            if (!rsCard.next())
                return new CustomerResponseModel(ResultCode.CREDITCARD_NOT_FOUND);

            String queryInsert = "INSERT INTO customers(email, firstName, lastName, ccId, address) VALUES (?,?,?,?,?);";
            PreparedStatement psInsert = BillingService.getCon().prepareStatement(queryInsert);
            psInsert.setString(1, requestModel.getEmail());
            psInsert.setString(2, requestModel.getFirstName());
            psInsert.setString(3, requestModel.getLastName());
            psInsert.setString(4, ccId);
            psInsert.setString(5, requestModel.getAddress());
            psInsert.execute();
            return new CustomerResponseModel(ResultCode.SUCCESSFULLY_INSERTED_CUSTOMER);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Customer:: failure to insert customer.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
            return new CustomerResponseModel(ResultCode.DUPLICATE_CUSTOMER_INSERT);
        }
    }

    public static CustomerResponseModel updateCustomerFrom(CustomerRequestModel requestModel){
        String ccId = requestModel.getCcId();

        ServiceLogger.LOGGER.info("Customer:: received update for customer with ccId: "+ccId);

        int idCode = HelpMe.verifyCreditCardID(ccId);
        if (idCode != 0)
            return new CustomerResponseModel(idCode);

        ServiceLogger.LOGGER.info("Customer:: credit card ID is valid.");

        try{
            String queryCheck = "SELECT id FROM creditcards WHERE id = ?;";
            PreparedStatement psCheck = BillingService.getCon().prepareStatement(queryCheck);
            psCheck.setString(1, ccId);
            ResultSet rs = psCheck.executeQuery();
            if (!rs.next())
                return new CustomerResponseModel(ResultCode.CREDITCARD_NOT_FOUND);

            String query = "UPDATE customers SET firstName = ?, lastName = ?, address = ? WHERE email = ? AND ccId = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getFirstName());
            ps.setString(2, requestModel.getLastName());
            ps.setString(3, requestModel.getAddress());
            ps.setString(4, requestModel.getEmail());
            ps.setString(5, ccId);
            if (ps.executeUpdate() != 0)
                return new CustomerResponseModel(ResultCode.SUCCESSFULLY_UPDATED_CUSTOMER);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Customer:: failure to update customer.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new CustomerResponseModel(ResultCode.CUSTOMER_NOT_EXIST);
    }

    public static RetrieveCustomerResponseModel retrieveCustomerFromDB(RetrieveCustomerRequestModel requestModel){
        String email = requestModel.getEmail();

        ServiceLogger.LOGGER.info("Customer:: received retrieve for email: "+email);

        try{
            String query = "SELECT email, firstName, lastName, ccId, address FROM customers WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return new RetrieveCustomerResponseModel(ResultCode.CUSTOMER_NOT_EXIST);
            CustomerModel cm = new CustomerModel(rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("ccID"), rs.getString("address"));
            RetrieveCustomerResponseModel rm = new RetrieveCustomerResponseModel(ResultCode.SUCCESSFULLY_RETRIEVED_CUSTOMER);
            rm.setCustomer(cm);
            return rm;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Customer:: failure to retrieve customer");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new RetrieveCustomerResponseModel(ResultCode.CUSTOMER_NOT_EXIST);
    }
}
