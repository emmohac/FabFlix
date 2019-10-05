package edu.uci.ics.huymt2.service.billing.logicalhandler;

import edu.uci.ics.huymt2.service.billing.BillingService;
import edu.uci.ics.huymt2.service.billing.core.HelpMe;
import edu.uci.ics.huymt2.service.billing.core.Item;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.models.*;
import edu.uci.ics.huymt2.service.billing.models.shoppingcart.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoppingCart {
    public static ShoppingCartResponseModel insertSCartToDBFrom(ShoppingCartRequestModel requestModel){
        String email = requestModel.getEmail();

        ServiceLogger.LOGGER.info("shoppingcart:: received insert shopping cart for email: "+email);

        int resultCode = HelpMe.validateRequestModel(requestModel);
        if (resultCode != 0)
            return new ShoppingCartResponseModel(resultCode);

        try{
            String query = "INSERT INTO carts(email, movieId, quantity) VALUES (?,?,?);";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, requestModel.getMovieId());
            ps.setInt(3, requestModel.getQuantity());
            ps.execute();
            return new ShoppingCartResponseModel(ResultCode.SUCCESSFULLY_INSERTED);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("shoppingcart:: failure to insert shopping cart");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new ShoppingCartResponseModel(ResultCode.DUPLICATE_INSERT);
    }

    public static ShoppingCartResponseModel updateSCartToDBFrom(ShoppingCartRequestModel requestModel){
        String email = requestModel.getEmail();

        ServiceLogger.LOGGER.info("shoppingcart:: received update shopping cart for email: "+email);

        int resultCode = HelpMe.validateRequestModel(requestModel);
        if (resultCode != 0)
            return new ShoppingCartResponseModel(resultCode);

        try{
            String query = "UPDATE carts SET quantity = ? WHERE email = ? AND movieId = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setInt(1, requestModel.getQuantity());
            ps.setString(2, email);
            ps.setString(3, requestModel.getMovieId());
            ps.execute();
            return new ShoppingCartResponseModel(ResultCode.SUCCESSFULLY_UPDATED);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("shoppingcart:: failure to update shopping cart.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new ShoppingCartResponseModel(ResultCode.ITEM_NOT_EXISTED);
    }

    public static ShoppingCartResponseModel deleteSCartInDB(DeleteSCartRequestModel requestModel){
        String email = requestModel.getEmail();
        String movieId = requestModel.getMovieId();

        ServiceLogger.LOGGER.info("shoppingcart:: received delete shopping cart for email: "+email);

        if (email.length() == 0 || email.length() > 50)
            return new ShoppingCartResponseModel(ResultCode.INVALID_LENGTH);

        if (!HelpMe.isEmailFormatted(email))
            return new ShoppingCartResponseModel(ResultCode.INVALID_FORMAT);

        try{
            String queryCheck = "SELECT * FROM carts WHERE email = ? AND movieId = ?;";
            PreparedStatement psCheck = BillingService.getCon().prepareStatement(queryCheck);
            psCheck.setString(1, email);
            psCheck.setString(2, movieId);
            ResultSet rs = psCheck.executeQuery();

            if (!rs.next())
                return new ShoppingCartResponseModel(ResultCode.ITEM_NOT_EXISTED);

            String query = "DELETE FROM carts WHERE email = ? AND movieId = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, movieId);
            ps.execute();
            return new ShoppingCartResponseModel(ResultCode.ITEM_DELETED);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("shoppingcart:: failure to delete shopping cart.");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new ShoppingCartResponseModel(ResultCode.ITEM_NOT_EXISTED);
    }

    public static RetrieveSCartResponseModel retrieveSCartFromDB(RetrieveSCartRequestModel requestModel){
        String email = requestModel.getEmail();
        ServiceLogger.LOGGER.info("shoppingcart:: received retrieve shopping cart for email: "+email);

        if (email.length() == 0 || email.length() > 50)
            return new RetrieveSCartResponseModel(ResultCode.INVALID_LENGTH);

        if (!HelpMe.isEmailFormatted(email))
            return new RetrieveSCartResponseModel(ResultCode.INVALID_FORMAT);

        ArrayList<Item> items = HelpMe.retrieveItemFromDB(email);
        if (items == null)
            return new RetrieveSCartResponseModel(ResultCode.ITEM_NOT_EXISTED);

        int len = items.size();
        ItemModel[] array = new ItemModel[len];

        for (int i = 0; i < len; ++i)
            array[i] =  ItemModel.buildModelFromObject(items.get(i));
        RetrieveSCartResponseModel rm = new RetrieveSCartResponseModel(ResultCode.ITEM_RETRIEVED);
        rm.setItems(array);
        return rm;
    }

    public static ShoppingCartResponseModel clearSCartFromDB(RetrieveSCartRequestModel requestModel){
        String email = requestModel.getEmail();

        ServiceLogger.LOGGER.info("shoppingcart:: received clear shopping cart for email: "+email);
        if (email.length() == 0 || email.length() > 50)
            return new ShoppingCartResponseModel(ResultCode.INVALID_LENGTH);

        if (!HelpMe.isEmailFormatted(email))
            return new ShoppingCartResponseModel((ResultCode.INVALID_FORMAT));

        try{
            String query = "DELETE FROM carts WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ps.execute();
            return new ShoppingCartResponseModel(ResultCode.ITEM_CLEARED);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("shoppingcart:: failure to clear shopping cart");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new ShoppingCartResponseModel(ResultCode.SCART_NOT_FOUND);
    }
}
