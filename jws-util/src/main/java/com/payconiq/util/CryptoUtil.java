package com.payconiq.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.util.Base64;

class CryptoUtil {

    public static PublicKey getPublicKeyFromCert(String certificate) throws Exception {
        InputStream certStream = new ByteArrayInputStream(decodeKey(certificate));
        java.security.cert.Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certStream);
        return cert.getPublicKey();
    }

    private static byte[] decodeKey(String key) {
        return Base64.getDecoder().decode(key
                .replaceAll("\\n", "")
                .replaceAll("-----BEGIN (PUBLIC|PRIVATE) KEY-----", "")
                .replaceAll("-----END (PUBLIC|PRIVATE) KEY-----", "")
                .replaceAll("\\s", ""));
    }
}
