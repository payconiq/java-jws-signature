package com.payconiq.util;

public class CertificateKeys {
    private CertificateKey[] keys;


    public static class CertificateKey {
        private String kid;
        private String alg;
        private String kty;
        private String use;
        private String[] x5c;

        public String getKid() {
            return kid;
        }

        public String[] getX5c() {
            return x5c;
        }
    }



    public CertificateKey[] getKeys()
    {
        return keys;
    }
}