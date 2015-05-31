package main.java.com.pocadyen.wrappers;

/**
 * Created by Lohan Bodevan on 31/05/15.
 */
public class CaptureResponseWrapper {
    private String pspReference;
    private String response;

    private String erroCode;
    private String message;

    public String getPspReference() {
        return pspReference;
    }

    public void setPspReference(String pspReference) {
        this.pspReference = pspReference;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErroCode() {
        return erroCode;
    }

    public void setErroCode(String erroCode) {
        this.erroCode = erroCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
