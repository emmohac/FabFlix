package edu.uci.ics.huymt2.service.billing.logicalhandler;

import edu.uci.ics.huymt2.service.billing.BillingService;
import edu.uci.ics.huymt2.service.billing.core.HelpMe;
import edu.uci.ics.huymt2.service.billing.core.Item;
import edu.uci.ics.huymt2.service.billing.core.PayPalClient;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;
import edu.uci.ics.huymt2.service.billing.models.*;
import edu.uci.ics.huymt2.service.billing.models.order.*;
import edu.uci.ics.huymt2.service.billing.models.shoppingcart.RetrieveSCartRequestModel;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class Order {
    public static OrderPlaceResponseModel placeOrderFrom(OrderRequestModel requestModel){
        String email = requestModel.getEmail();

        ServiceLogger.LOGGER.info("Order:: received place order for email: "+email);
        try{
            String customerQuery = "SELECT email FROM customers WHERE email = ?;";
            PreparedStatement psCustomer = BillingService.getCon().prepareStatement(customerQuery);
            psCustomer.setString(1, email);
            ResultSet rsCustomer = psCustomer.executeQuery();

            if (!rsCustomer.next())
                return new OrderPlaceResponseModel(ResultCode.CUSTOMER_NOT_EXIST);
            ServiceLogger.LOGGER.info(email + " exists as customer");
            String cartQuery = "SELECT id, movieId, quantity FROM carts WHERE email = ?;";
            PreparedStatement psCart = BillingService.getCon().prepareStatement(cartQuery);
            psCart.setString(1, email);
            ResultSet rsCart = psCart.executeQuery();

            if (!rsCart.next())
                return new OrderPlaceResponseModel(ResultCode.SCART_NOT_FOUND);
            ServiceLogger.LOGGER.info("Shopping cart is not empty for: "+email);

            float sum = 1;
            rsCart.beforeFirst();

            while (rsCart.next()){
                String query = "SELECT unit_price, discount FROM movie_prices WHERE movieId = ?;";
                PreparedStatement ps = BillingService.getCon().prepareStatement(query);
                ps.setString(1, rsCart.getString("movieId"));
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    sum += rs.getFloat("unit_price") * rs.getFloat("discount") * (float) rsCart.getInt("quantity");
            }

            ServiceLogger.LOGGER.info("Order: calculated sum successfully.");
            PayPalClient client = new PayPalClient();
            DecimalFormat format = new DecimalFormat("0.00");
            String total = format.format(sum);
            Map<String, Object> map = client.createdPayment(total);

            String redirectURL = (String) map.get("redirect_url");
            ServiceLogger.LOGGER.info("Redirect URL: "+redirectURL);
            if (map.get("status") != "success")
                return new OrderPlaceResponseModel(ResultCode.PAYMENT_FAILED);

            String[] str = redirectURL.split("=");
            String token = str[str.length-1];
            ServiceLogger.LOGGER.info("Token: "+token);

            rsCart.beforeFirst();
            while (rsCart.next()){
                String query = "{call insert_sales_transactions(?, ?, ?, ?)}";
                CallableStatement cs = BillingService.getCon().prepareCall(query);
                cs.setString(1, email);
                cs.setString(2, rsCart.getString("movieId"));
                cs.setInt(3, rsCart.getInt("quantity"));
                cs.setString(4, token);
                cs.execute();
            }
            ServiceLogger.LOGGER.info("Order: inserted into sales and transaction.");

            RetrieveSCartRequestModel rm = new RetrieveSCartRequestModel(email);
            ShoppingCart.clearSCartFromDB(rm);

            OrderPlaceResponseModel toReturn = new OrderPlaceResponseModel(ResultCode.SUCCESSFULLY_PLACED_ORDER);
            toReturn.setToken(token);
            toReturn.setRedirectURL(redirectURL);
            return toReturn;
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Order:: failure to place an order");
            ServiceLogger.LOGGER.info(e.getClass().getSimpleName());
        }
        return new OrderPlaceResponseModel(ResultCode.SCART_NOT_FOUND);
    }

    public static OrderRetrieveResponseModel retrieveOrderFromDB(OrderRequestModel requestModel){
        String email = requestModel.getEmail();

        ServiceLogger.LOGGER.info("Order:: received retrieve order for email: "+email);

        ArrayList<String> trans = HelpMe.retrieveTransactionIDFromDB(email);

        if (trans == null || trans.size() == 0)
            return new OrderRetrieveResponseModel(ResultCode.CUSTOMER_NOT_EXIST);

        int len = trans.size();
        TransactionModel[] toReturn = new TransactionModel[len];
        PayPalClient client = new PayPalClient();

        for (int i = 0; i < len; ++i){
            String transId = trans.get(i);

            Map<String, Object> map = client.retrievePayment(transId);

            AmountModel am = new AmountModel((String) map.get("amountTotal"), (String) map.get("amountCurrency"));
            TransactionFeeModel tm = new TransactionFeeModel((String) map.get("transactionValue"), (String) map.get("transactionCurrency"));

            ArrayList<Item> items = HelpMe.retrieveItemForTransID(transId, email);
            int length = items.size();
            ItemModel[] itemReturn = new ItemModel[length];

            for (int j = 0; j < length; ++j)
                itemReturn[j] = ItemModel.buildModelFromAdvancedObject(items.get(j));

            toReturn[i] = new TransactionModel(transId, (String) map.get("state"),
                    am, tm, (String) map.get("create_time"), (String) map.get("update_time"), itemReturn);
        }
        OrderRetrieveResponseModel rm = new OrderRetrieveResponseModel(ResultCode.SUCCESSFULLY_RETREIVED_ORDER);
        rm.setTransactions(toReturn);
        return rm;
    }

    public static OrderResponseModel completeOrderFrom(OrderCompleteRequestModel requestModel){
        String paymentId = requestModel.getPaymentId();
        String token = requestModel.getToken();
        String payerId = requestModel.getPayerId();

        PayPalClient client = new PayPalClient();
        Map<String, Object> map = client.completePayment(payerId, paymentId);
        String transactionId = (String) map.get("transactionID");
        if (map.get("status") != "success")
            return new OrderResponseModel(ResultCode.PAYMENT_NOT_COMPLETED);
        try{
            String query = "UPDATE transactions SET transactionId = ? WHERE token = ?;";
            PreparedStatement ps = BillingService.getCon().prepareStatement(query);
            ps.setString(1, transactionId);
            ps.setString(2, token);
            int toCheck = ps.executeUpdate();

            if (toCheck == 0)
                return new OrderResponseModel(ResultCode.TOKEN_NOT_FOUND);

            return new OrderResponseModel(ResultCode.PAYMENT_COMPLETED);
        }catch (SQLException e){
            ServiceLogger.LOGGER.info("Error print out later");
        }
        return new OrderResponseModel(ResultCode.PAYMENT_NOT_COMPLETED);
    }
}
