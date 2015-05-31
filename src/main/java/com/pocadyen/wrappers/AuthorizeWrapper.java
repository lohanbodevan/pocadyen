package main.java.com.pocadyen.wrappers;

import java.util.Map;

/**
 * Created by Lohan Bodevan on 30/05/15.
 *
 * Wrapper for JSON:
 * {
 *      "amount": {
 *           "currency": "BRL",
 *           "value": "100"
 *       },
 *       "card": {
 *           "expiryMonth": "06",
 *           "expiryYear": "2016",
 *           "holderName": "John Smith",
 *           "number": "4111111111111111",
 *           "cvc": "737"
 *       },
 *       "reference": "reference for authorize"
 * }
 */
public class AuthorizeWrapper {
    private Map<String, Object> amount;
    private Map<String, Object> card;
    private String reference;

    public Map<String, Object> getAmount() {
        return amount;
    }

    public void setAmount(Map<String, Object> amount) {
        this.amount = amount;
    }

    public Map<String, Object> getCard() {
        return card;
    }

    public void setCard(Map<String, Object> card) {
        this.card = card;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
