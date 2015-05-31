package main.java.com.pocadyen.models;

import main.java.com.pocadyen.wrappers.AuthorizeResponseWrapper;
import main.java.com.pocadyen.wrappers.AuthorizeWrapper;

import main.java.com.pocadyen.wrappers.CaptureResponseWrapper;
import main.java.com.pocadyen.wrappers.CaptureWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Lohan Bodevan on 30/05/15.
 */
public class PaymentRequestModel {
    //@TODO Transform in environment variables
    private String apiAuthorizeUrl = "https://pal-test.adyen.com/pal/servlet/Payment/v10/authorise";
    private String apiCaptureUrl = "https://pal-test.adyen.com/pal/servlet/Payment/v12/capture";
    private String wsUser = "";
    private String wsPassword = "";
    private String merchantAccount = "";

    //HTTP Request and Response objects
    private HttpClient client;
    private HttpResponse httpResponse;
    private JSONObject paymentResult;

    public PaymentRequestModel() {
        if (this.client == null) {
            //Create Authentication with webservice
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(this.wsUser, this.wsPassword);
            provider.setCredentials(AuthScope.ANY, credentials);

            this.client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        }
    }

    public AuthorizeResponseWrapper authorize(AuthorizeWrapper authorize) {
        // Create new authorize payment Request
        JSONObject paymentRequest = new JSONObject();
        paymentRequest.put("merchantAccount", this.merchantAccount);
        paymentRequest.put("reference", authorize.getReference());
        paymentRequest.put("fraudOffset", "0");

        // Set amount
        JSONObject amount = new JSONObject();
        amount.put("currency", authorize.getAmount().get("currency"));
        amount.put("value", authorize.getAmount().get("value"));
        paymentRequest.put("amount", amount);

        // Set card
        JSONObject card = new JSONObject();
        card.put("expiryMonth", authorize.getCard().get("expiryMonth"));
        card.put("expiryYear", authorize.getCard().get("expiryYear"));
        card.put("holderName", authorize.getCard().get("holderName"));
        card.put("number", authorize.getCard().get("number"));
        card.put("cvc", authorize.getCard().get("cvc"));
        paymentRequest.put("card", card);

        /**
         * Do the request
         * if the request is successful a httpResponse object is created with Response Headers and
         * a paymentResult object is created with API result
         */
        try {
            this.doRequest(paymentRequest, this.apiAuthorizeUrl);
        } catch (IOException e) {
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.SEVERE, "Authorize request error");
        }

        AuthorizeResponseWrapper responseWrapper = new AuthorizeResponseWrapper();
        responseWrapper.setPspReference((String) paymentResult.get("pspReference"));
        responseWrapper.setResultCode((String) paymentResult.get("resultCode"));
        responseWrapper.setAuthCode((String) paymentResult.get("authCode"));
        responseWrapper.setRefusalReason((String) paymentResult.get("refusalReason"));

        // If the request was rejected, raise an exception
        if (this.httpResponse.getStatusLine().getStatusCode() != 200) {
            responseWrapper.setResultCode((String) paymentResult.get("errorCode"));
            responseWrapper.setMessage((String) paymentResult.get("message"));
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.SEVERE, "Authorize error: " + paymentResult.get("errorType"));
        }

        return responseWrapper;
    }

    public CaptureResponseWrapper capture(CaptureWrapper captureWrapper) {
        // Create new capture payment Request
        JSONObject paymentRequest = new JSONObject();
        paymentRequest.put("merchantAccount", this.merchantAccount);
        paymentRequest.put("originalReference", captureWrapper.getOriginalReference());
        paymentRequest.put("reference", captureWrapper.getReference());

        // Set amount
        JSONObject modificationAmount = new JSONObject();
        modificationAmount.put("currency", captureWrapper.getModificationAmount().get("currency"));
        modificationAmount.put("value", captureWrapper.getModificationAmount().get("value"));

        paymentRequest.put("modificationAmount", modificationAmount);

        /**
         * Do the request
         * if the request is successful a httpResponse object is created with Response Headers and
         * a paymentResult object is created with API result
         */
        try {
            this.doRequest(paymentRequest, this.apiCaptureUrl);
        } catch (IOException e) {
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.SEVERE, "Capture request error");
        }

        CaptureResponseWrapper responseWrapper = new CaptureResponseWrapper();
        responseWrapper.setPspReference((String) paymentResult.get("pspReference"));
        responseWrapper.setResponse((String) paymentResult.get("response"));

        // If the doRequest was rejected, raise an exception
        if (this.httpResponse.getStatusLine().getStatusCode() != 200) {
            responseWrapper.setErroCode((String) paymentResult.get("errorCode"));
            responseWrapper.setMessage((String) paymentResult.get("message"));
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.SEVERE, "Capure error: " + paymentResult.get("errorType"));
        }

        return responseWrapper;
    }

    private void doRequest(JSONObject paymentRequest, String apiUrl) throws IOException{
        //Send HTTP Request
        HttpPost httpRequest = new HttpPost(apiUrl);
        httpRequest.addHeader("Content-Type", "application/json");
        httpRequest.setEntity(new StringEntity(paymentRequest.toString(), "UTF-8"));

        try {
            this.httpResponse = client.execute(httpRequest);
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.INFO, "Response: " + this.httpResponse);
        }catch (IOException ex) {
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.SEVERE, "Communication fault!", ex);
            throw new IOException("Service Unavailable");
        }

        String paymentResponse;

        try {
            paymentResponse = EntityUtils.toString(this.httpResponse.getEntity(), "UTF-8");
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.INFO, "Payment Response: " + paymentResponse);
        } catch (IOException ex) {
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.SEVERE, "Response to String parser error!", ex);
            throw new IOException("Internal Server Error");
        }

        // Parse JSON response
        JSONParser parser = new JSONParser();
        try {
            this.paymentResult = (JSONObject) parser.parse(paymentResponse);
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.INFO, "Payment Result: " + this.paymentResult);
        } catch (ParseException ex) {
            Logger.getLogger(PaymentRequestModel.class.getName()).log(Level.INFO, "JSON Parser error " + ex);
            throw new IOException("Internal Server Error");
        }
    }
}
