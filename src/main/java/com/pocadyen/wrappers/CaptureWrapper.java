package main.java.com.pocadyen.wrappers;

import java.util.Map;

/**
 * Created by Lohan Bodevan on 31/05/15.
 *
 * Wrapper for JSON:
 * {
 *      "modificationAmount": {
 *           "currency": "BRL",
 *           "value": "100"
 *       },
 *       "originalReference": "copy pspreference from authorise",
 *       "reference": "reference for capture"
 * }
 */
public class CaptureWrapper {
    private Map<String, Object> modificationAmount;
    private String originalReference;
    private String reference;

    public Map<String, Object> getModificationAmount() {
        return modificationAmount;
    }

    public void setModificationAmount(Map<String, Object> modificationAmount) {
        this.modificationAmount = modificationAmount;
    }

    public String getOriginalReference() {
        return originalReference;
    }

    public void setOriginalReference(String originalReference) {
        this.originalReference = originalReference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
