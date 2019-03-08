# Java Json Web Signature Utility

## **Introduction**
A utility used to validate the Json Web Signature sent in the header of a callback request. The utility can also validate the merchant product id and the callback url provided in the JOSE Header of the JWS.

## Usage
This example makes use of an existing Json Web Signature generated in the EXT environment. Passing the jws, environment, merchant product id, and callback url, the jws and JOSE header parameters can be validated.

The environments supported are `ext` and `prod`

JWSExample.java

```java

public class JWSExample
{
    
     public static void main(String[] args) {
        String body = "{\"paymentId\":\"687e0f4c373dbc4f381d31cd\",\"transferAmount\":700,\"tippingAmount\":0,\"amount\":700,\"totalAmount\":700,\"description\":\"stT\",\"reference\":\"Order1234\",\"createdAt\":\"2019-03-07T10:24:05.127Z\",\"expireAt\":\"2019-03-07T10:27:05.127Z\",\"status\":\"SUCCEEDED\",\"debtor\":{\"name\":\"Ivan\",\"iban\":\"***********42901\"},\"currency\":\"EUR\"}";
        String jws = "eyJ0eXAiOiJKT1NFK0pTT04iLCJraWQiOiJlcy5zaWduYXR1cmUuZXh0LnBheWNvbmlxLmNvbSIsImFsZyI6IkVTMjU2IiwiaHR0cHM6Ly9wYXljb25pcS5jb20vaWF0IjoiMjAxOS0wMy0wN1QxMDoyNDoyMy44ODBaIiwiaHR0cHM6Ly9wYXljb25pcS5jb20vanRpIjoiODE1MjgxZjczM2VjMTRmIiwiaHR0cHM6Ly9wYXljb25pcS5jb20vcGF0aCI6Imh0dHBzOi8vd2ViaG9vay5zaXRlLzI5ZGYwZjBmLTE2MDctNDUxYy1hMjU2LTM3NDI4YTQwM2U4YSIsImh0dHBzOi8vcGF5Y29uaXEuY29tL2lzcyI6IlBheWNvbmlxIiwiaHR0cHM6Ly9wYXljb25pcS5jb20vc3ViIjoiNWJiMzcyODRlMzVlMmIyOWUzNjNkZjIyIiwiY3JpdCI6WyJodHRwczovL3BheWNvbmlxLmNvbS9pYXQiLCJodHRwczovL3BheWNvbmlxLmNvbS9qdGkiLCJodHRwczovL3BheWNvbmlxLmNvbS9wYXRoIiwiaHR0cHM6Ly9wYXljb25pcS5jb20vaXNzIiwiaHR0cHM6Ly9wYXljb25pcS5jb20vc3ViIl19..5SvAcU4hGMoyCvbiazvNz7d3Ciwc3hRHjF1x14mPiEAVA1DDl3GZEDUGpBp7zns8pDzCYZHGaYlTQM2ABUABag";
        String environment = "ext";
        String merchantProductId = "5bb37284e35e2b29e363df22";
        String callbackUrl = "https://webhook.site/29df0f0f-1607-451c-a256-37428a403e8a";
        
        boolean result = ValidateJWS.validateJWSSignature(jws, body, environment);
        
        if(ValidateJWS.validateJWSSignature(jws, body, environment))
           System.out.println("JWS is valid");    
        else
           System.out.println("JWS is not valid");    
        
        if(ValidateJWS.validateJWSSignatureHeaders(jws, body, environment, callbackUrl, merchantProductId))
            System.out.println("JWS Headers are valid");
        else
            System.out.println("JWS Headers are not valid");
        
        if(ValidateJWS.validateJWSHeaderAndSignature(jws, body, environment, callbackUrl, merchantProductId))
            System.out.println("JWS & JWS Headers are valid");
        else
            System.out.println("JWS & JWS Headers are not valid");
     }    
}

```