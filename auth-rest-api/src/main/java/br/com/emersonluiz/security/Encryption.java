package br.com.emersonluiz.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Encryption {

    private Encryption() {
        throw new AssertionError();
    }

    public static String encrypt(String arg) throws NoSuchAlgorithmException{
        if (arg != null && arg != "") {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(arg.getBytes()));
            return String.format("%32x", hash).trim();
        }
        return arg;
    }

}
