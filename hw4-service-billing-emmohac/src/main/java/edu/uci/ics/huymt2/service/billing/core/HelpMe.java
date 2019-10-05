package edu.uci.ics.huymt2.service.billing.core;

import edu.uci.ics.huymt2.service.billing.BillingService;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.models.shoppingcart.ShoppingCartRequestModel;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HelpMe {
    public static int verifyCreditCardID(String id){
        ServiceLogger.LOGGER.info("HelpMe:: verifying credit card ID: "+id);

        if (id.length() != 19)
            return ResultCode.INVALID_CREDITCARD_LENGTH;

        for (int i = 0; i < id.length(); ++i)
            if (!Character.isDigit(id.charAt(i)))
                return ResultCode.INVALID_CREDITCARD_VALUE;

        return 0;
    }

    public static boolean verifyCreditCardExpiration(Date newExpiration){
        ServiceLogger.LOGGER.info("HelpMe:: verifying expiration date: "+newExpiration.toString());

        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        if (newExpiration.before(today))
            return false;
        return true;
    }

    public static int validateRequestModel(ShoppingCartRequestModel requestModel){
        String email = requestModel.getEmail();
        ServiceLogger.LOGGER.info("HelpMe:: validating requestModel having email: "+email);

        int quantity = requestModel.getQuantity();

        if (quantity <= 0)
            return ResultCode.INVALID_QUANTITY;

        if (email.length() == 0 || email.length() > 50)
            return ResultCode.INVALID_LENGTH;

        if (!isEmailFormatted(email))
            return ResultCode.INVALID_FORMAT;

        return 0;
    }

    public static ArrayList<OrderDetail> retrieveOrderFromDB(String email){
        ServiceLogger.LOGGER.info("HelpMe::retrieving order from DB for email: "+email);

        try{
            String query = "SELECT email, movieId, quantity, saleDate FROM sales WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return null;
            ArrayList<OrderDetail> toReturn = new ArrayList<>();
            rs.beforeFirst();
            while (rs.next()) {
                toReturn.add(new OrderDetail(rs.getString("email"), rs.getString("movieId"), rs.getInt("quantity"), rs.getDate("saleDate")));
            }
            return toReturn;
        }catch (SQLException e) {
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve order from DB");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    public static ArrayList<String> retrieveTransactionIDFromDB(String email){
        ServiceLogger.LOGGER.info("HelpMe:: retrieving transaction from DB for email "+email);

        try{
            String query = "SELECT DISTINCT transactionId FROM transactions, sales WHERE sales.id = transactions.sId AND sales.email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> trans = new ArrayList<>();
            while (rs.next())
                trans.add(rs.getString("transactionId"));
            ServiceLogger.LOGGER.info("HelpMe:: transaction length: "+trans.size());
            return trans;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve transactionID from DB");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    public static ArrayList<Item> retrieveItemFromDB(String email){
        ServiceLogger.LOGGER.info("HelpMe:: retrieving item from DB for email: "+email);

        try{
            String query = "SELECT email, movieId, quantity FROM carts WHERE email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return null;
            ArrayList<Item> toReturn = new ArrayList<>();
            rs.beforeFirst();
            while (rs.next())
                toReturn.add(new Item(rs.getString("email"), rs.getString("movieId"), rs.getInt("quantity")));
            return toReturn;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve item from DB");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    public static ArrayList<Item> retrieveItemForTransID(String transID, String email){
        ServiceLogger.LOGGER.info("HelpMe:: retrieving item for transactionID "+ transID);
        ArrayList<Item> items = new ArrayList<>();
        try{
            String query = "SELECT sales.movieId, sales.quantity, movie_prices.unit_price, movie_prices.discount, sales.saleDate " +
                    "FROM sales, movie_prices, transactions " +
                    "WHERE transactionId = ? AND sales.id = transactions.Sid AND sales.movieId = movie_prices.movieId AND sales.email = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, transID);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            DecimalFormat df = new DecimalFormat("0.00");
            while (rs.next())
                items.add(new Item(email, rs.getString("movieId"), rs.getInt("quantity"), Float.parseFloat(df.format(rs.getFloat("unit_price"))), Float.parseFloat(df.format(rs.getFloat("discount"))), rs.getDate("saleDate").toString()));
            return items;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("HelpMe:: failure to retrieve item for transactionId"+transID);
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return null;
    }

    public static boolean isEmailFormatted(String e){
        int countAt = 0;
        for (int i = 0; i < e.length(); ++i)
            if (e.charAt(i) == '@')
                ++countAt;

        if (countAt != 1)
            return false;

        String[] toCheck = e.split("@");
        String prefix = toCheck[0];
        String domain = toCheck[1];

        int dot = 0;
        for (int i = 0 ; i < domain.length(); ++i)
            if (domain.charAt(i) == '.')
                ++dot;

        if (dot == 0)
            return false;

        if (prefix.isEmpty() || domain.isEmpty() || !isWellFormatted(prefix) || !isWellFormatted(domain))
            return false;

        return true;
    }

    private static boolean isWellFormatted(String prefix){
        if (prefix.endsWith("-") || prefix.startsWith(".")
                || prefix.startsWith("-") || prefix.endsWith(".")
                || hasContiguousDot(prefix))
            return false;
        return true;
    }

    private static boolean hasContiguousDot(String s){
        for (int i = 0; i < s.length()-1; ++i)
            if (s.indexOf(i) == '.' && s.indexOf(i+1) == '.')
                return true;

        return false;
    }
}
