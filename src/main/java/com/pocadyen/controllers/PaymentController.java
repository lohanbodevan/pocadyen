package main.java.com.pocadyen.controllers;

import main.java.com.pocadyen.models.PaymentRequestModel;
import main.java.com.pocadyen.wrappers.AuthorizeResponseWrapper;
import main.java.com.pocadyen.wrappers.AuthorizeWrapper;
import main.java.com.pocadyen.wrappers.CaptureResponseWrapper;
import main.java.com.pocadyen.wrappers.CaptureWrapper;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Lohan Bodevan on 30/05/15.
 */

@RestController
@EnableAutoConfiguration
public class PaymentController {

    @RequestMapping(method=RequestMethod.POST, value="/authorize")
    public AuthorizeResponseWrapper authorize(@RequestBody AuthorizeWrapper auth) {
        PaymentRequestModel paymentModel = new PaymentRequestModel();
        return paymentModel.authorize(auth);
    }

    @RequestMapping(method=RequestMethod.POST, value="/capture")
    public CaptureResponseWrapper capture(@RequestBody CaptureWrapper capture) {
        PaymentRequestModel paymentModel = new PaymentRequestModel();
        return paymentModel.capture(capture);
    }
}
