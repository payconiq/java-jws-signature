package com.payconiq.util;

import com.google.gson.Gson;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.Arrays;


public class Util {

    static Logger logger = LoggerFactory.getLogger(Util.class);

    private static final String[] critSignatureHeaders = {
            "https://payconiq.com/iat",
            "https://payconiq.com/jti",
            "https://payconiq.com/path",
            "https://payconiq.com/iss",
            "https://payconiq.com/sub"
    };

    private static final String extUrlConfig = "https://ext.payconiq.com/certificates";
    private static final String prodUrlConfig = "https://payconiq.com/certificates";

    private static final String callbackUrl = "https://payconiq.com/path";
    private static final String subjectUrl = "https://payconiq.com/sub";

    private static final String extKid = "es.signature.ext.payconiq.com";
    private static final String prodKid = "es.signature.payconiq.com";

    private static final String prodUrlString = "prod";
    private static final String extUrlString = "ext";


    private static String getCertificateFromUrl(String url, String kid) throws Exception {
        try {
            Gson gson = new Gson();
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {

                CertificateKeys keys = gson.fromJson(new InputStreamReader(connection.getInputStream()), CertificateKeys.class);

                CertificateKeys.CertificateKey key = Arrays.stream(keys.getKeys())
                        .filter(x -> x.getKid().equals(kid)).findFirst().get();
                return key.getX5c()[0];
            } else {
                throw new Exception(String.format("Cannot get data from url '%s'.", url));
            }

        } catch (Exception ex) {
            throw new Exception(String.format("Cannot get data from url '%s'.", url));
        }
    }

    public static JsonWebSignature getJws(String jwsSignature, String callbackBody, String environment) throws Exception {

        PublicKey publicKey = getPublicKeyFromHost(environment);
        logger.info("Public Key successfully retrieved.");

        logger.info("Verifying JsonWebSignature");
        JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST,
                AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256));
        jws.setCompactSerialization(jwsSignature);
        jws.setKey(publicKey);
        jws.setEncodedPayload(null);
        jws.setPayload(callbackBody);
        jws.setKnownCriticalHeaders(Util.getCritSignatureHeaders());
        return jws;
    }

    private static PublicKey getPublicKeyFromHost(String environment) throws Exception {
        logger.info("Retrieving Public Key from the {} environment", environment);

        String certificatesUrl = null;
        String keyId = null;

        if (environment.equalsIgnoreCase(Util.getProdUrlString())) {
            certificatesUrl = Util.getProdUrlConfig();
            keyId = Util.getProdKid();
        } else if (environment.equalsIgnoreCase(Util.getExtUrlString())) {
            certificatesUrl = Util.getExtUrlConfig();
            keyId = Util.getExtKid();
        }
        else
            throw new Exception("The environment provided must either be 'ext' or 'prod'");

        String configCert = getCertificateFromUrl(certificatesUrl, keyId);
        return CryptoUtil.getPublicKeyFromCert(configCert);
    }

    private static String[] getCritSignatureHeaders() {
        return critSignatureHeaders;
    }

    private static String getExtUrlConfig() {
        return extUrlConfig;
    }

    private static String getProdUrlConfig() {
        return prodUrlConfig;
    }

    private static String getExtKid() {
        return extKid;
    }

    private static String getProdKid() {
        return prodKid;
    }

    private static String getProdUrlString() {
        return prodUrlString;
    }

    private static String getExtUrlString() {
        return extUrlString;
    }

    public static String getCallbackUrl() {
        return callbackUrl;
    }

    public static String getSubjectUrl() {
        return subjectUrl;
    }
}
