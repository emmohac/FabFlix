package edu.uci.ics.huymt2.service.billing.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import edu.uci.ics.huymt2.service.billing.BillingService;
import edu.uci.ics.huymt2.service.billing.logger.ServiceLogger;

import javax.ws.rs.core.UriBuilder;

public class PayPalClient {
    private String clientId = "ASw9EexE4d1o75qZyG2NepxGvp_aGYgxpfOaUQGgrIXMa3v3C4XozAglcR5a6LhPCbGwC_7L3KgMZPha";
    private String clientSecret = "EPv7ZHVoRoNqreJ9FZgeydCDrrTAfRvbqtCO7r2EWe--_hYg8w611EQCx6ENEa3FVTaIdQpmuvfYFcY4";

    public PayPalClient() {}

    public Map<String, Object> createdPayment(String sum){
        Map<String, Object> response = new HashMap<String, Object>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        String scheme = BillingService.getConfigs().getScheme();
        String hostName = BillingService.getConfigs().getHostName();
        int port = BillingService.getConfigs().getPort();
        String path = BillingService.getConfigs().getPath();
        URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();
        redirectUrls.setReturnUrl(uri.toString()+"/order/complete");
        ServiceLogger.LOGGER.info("returnURL: ");
        redirectUrls.setCancelUrl(uri.toString());
        payment.setRedirectUrls(redirectUrls);
        ServiceLogger.LOGGER.info(payment.getRedirectUrls().toString());
        Payment createdPayment;
        try{
            String redirectUrl = "";
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(context);

            if (createdPayment != null){
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
            else
                response.put("status", "failed");
        }catch (PayPalRESTException e){
            ServiceLogger.LOGGER.info("Error happened during payment creation");
        }
        return response;
    }

    public Map<String, Object> completePayment(String payerId, String paymentId){
        Map<String, Object> response = new HashMap<>();
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        try{
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            String transactionID = createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
            if (createdPayment != null){
                response.put("status", "success");
                response.put("payment", createdPayment);
                response.put("transactionID", transactionID);
            }
        }catch (PayPalRESTException e){
            ServiceLogger.LOGGER.info("Error happened during payment creation.");
        }
        return response;
    }

    public Map<String, Object> retrievePayment(String transID){
        Map<String, Object> response = new HashMap<>();
        try{
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            Sale sale = Sale.get(apiContext, transID);
            if (sale != null){
                Amount amount = sale.getAmount();
                Currency currency = sale.getTransactionFee();
                response.put("state", sale.getState());
                response.put("amountTotal", amount.getTotal());
                response.put("amountCurrency", amount.getCurrency());
                response.put("transactionValue", currency.getValue());
                response.put("transactionCurrency", currency.getCurrency());
                response.put("create_time", sale.getCreateTime());
                response.put("update_time", sale.getUpdateTime());
            }
        }catch (PayPalRESTException e){
            ServiceLogger.LOGGER.info("Error happened during payment retrieving");
        }
        return response;
    }

}
