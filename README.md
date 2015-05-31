#This is a simple POC of the integration with [ADYEN] (https://www.adyen.com) Gateway in Java

##Webservice authentication
You'll need to create a test account in [Adyen website] (https://www.adyen.com/home/discover/test-account-signup#form).

They'll provide to you a webservice user, webservice password and a merchant account code.

##Installation and Run
```
Execute "start.sh" file
The API run in http://localhost:8080
```

###POST
/authorize

Example:
```
{
    "amount": {
        "currency": "BRL",
        "value": "10000"
    },
    "card": {
        "expiryMonth": "6",
        "expiryYear": "2016",
        "holderName": "John Smith",
        "number": "4111111111111111",
        "cvc": "737"
    },
    "reference": "order-id-1"
 }
 ```
/capture

Example:
```
{
    "modificationAmount": {
        "currency": "BRL",
        "value": "10000"
    },
    "originalReference": "copy pspreference from authorise",
    "reference": "order-id-1"
}
```

This application use [Gradle] (https://www.gradle.org) to build and deploy.

Author: [Lohan Bodevan] (mailto:lohan.bodevan@gmail.com)
