package com.payconiq.jws;

import com.payconiq.util.Util;
import org.jose4j.jws.JsonWebSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  ValidateJWS class used to validate a Payconiq generated Json Web Signature
 */
class ValidateJWS {

    public static Logger logger = LoggerFactory.getLogger(ValidateJWS.class);


    /**
     * This function validates the Json Web Signature provided.
     * @param jwsSignature The Json Web Signature sent by Payconiq.
     * @param callbackBody The body of the callback request sent by Payconiq.
     * @param environment The environment from which the callback was generated. 'ext' for External and 'prod' for Production
     * @return true or false indicating if a Json Web Signature is valid or note. True = JWS is valid. False = JWS is not valid.
     * @throws Exception
     */
    public static boolean validateJWSSignature(String jwsSignature, String callbackBody, String environment) throws Exception {
        logger.info("Request received to validate JWS Signature");
        JsonWebSignature jws = Util.getJws(jwsSignature, callbackBody, environment);
        logger.info("Is JsonWebSignature Valid? {}", jws.verifySignature());
        return jws.verifySignature();
    }

    /**
     * This function validates the merchantProfileId and the callbackUrl in the JOSE Header of the Json Web Signature.
     * @param jwsSignature The Json Web Signature sent by Payconiq.
     * @param callbackBody The body of the callback request sent by Payconiq.
     * @param environment The environment from which the callback was generated. 'ext' for External and 'prod' for Production
     * @param callbackUrl The callback url configured in the payment
     * @param merchantProfileId The merchant profile id provided by Payconiq.
     * @return true or false indicating if the merchantProfileId and callbackUrl match the values in the JOSE Header or not. True = JWS is valid. False = JWS is not valid.
     * @throws Exception
     */
    public static boolean validateJWSSignatureHeaders(String jwsSignature, String callbackBody, String environment,
                                                      String callbackUrl, String merchantProfileId)
            throws Exception {
        JsonWebSignature jws = Util.getJws(jwsSignature, callbackBody, environment);

        String callbackUrlHeaderValue = jws.getHeaders().getStringHeaderValue(Util.getCallbackUrl());
        String subHeaderValue = jws.getHeaders().getStringHeaderValue(Util.getSubjectUrl());

        return subHeaderValue.equalsIgnoreCase(merchantProfileId) && callbackUrlHeaderValue.equalsIgnoreCase(callbackUrl);
    }

    /**
     * This function validates the Json Web Signature, the merchantProfileId and the callbackUrl in the Json Web Signature.
     * @param jwsSignature The Json Web Signature sent by Payconiq.
     * @param callbackBody The body of the callback request sent by Payconiq.
     * @param environment The environment from which the callback was generated. 'ext' for External and 'prod' for Production
     * @param callbackUrl The callback url configured in the payment
     * @param merchantProfileId The merchant profile id provided by Payconiq.
     * @return true or false indicating if the merchantProfileId and callbackUrl match the values in the JOSE Header and the Json Web Signature is valid.
     *                               true = Headers match and JWS is valid. false = Headers don't match or JWS is not valid.
     * @throws Exception
     */
    public static boolean validateJWSHeaderAndSignature(String jwsSignature, String callbackBody, String environment,
                                                        String callbackUrl, String merchantProfileId)
            throws Exception {
        boolean jwsValidation = validateJWSSignature(jwsSignature, callbackBody, environment);

        boolean headerValidation = validateJWSSignatureHeaders(jwsSignature, callbackBody, environment,
                callbackUrl, merchantProfileId);

        return jwsValidation && headerValidation;
    }
}
