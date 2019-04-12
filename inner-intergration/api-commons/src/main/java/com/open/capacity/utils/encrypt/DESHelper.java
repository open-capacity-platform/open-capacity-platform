package com.open.capacity.utils.encrypt;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DESHelper {
    private static final String DES = "DES";
    private static final String KEY = "www.com.cn";

    public static String encrypt(final String pliantext) throws Exception {
        return encodeBase64(encryptDES(pliantext, "www.com.cn"));
    }

    public static String encrypt(final String pliantext, final String key) throws Exception {
        return encodeBase64(encryptDES(pliantext, key));
    }

    public static String decrypt(final String ciphertext) throws Exception {
        return decryptDES(decodeBase64(ciphertext.getBytes()), "www.com.cn");
    }

    public static String decrypt(final String ciphertext, final String key) throws Exception {
        return decryptDES(decodeBase64(ciphertext.getBytes()), key);
    }

    private static String encodeBase64(final byte[] binaryData) throws Exception {
        try {
            return Base64.encodeBase64String(binaryData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BASE64\u7f16\u7801\u5931\u8d25!");
        }
    }

    private static byte[] decodeBase64(final byte[] binaryData) {
        try {
            return Base64.decodeBase64(binaryData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BASE64\u89e3\u7801\u5931\u8d25\uff01");
        }
    }

    public static byte[] encryptDES(final String data, final String key) {
        try {
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            final DESKeySpec deskey = new DESKeySpec(key.getBytes("UTF-8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey secretKey = keyFactory.generateSecret(deskey);
            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, secretKey, random);
            return cipher.doFinal(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptDES(final byte[] data, final String key) {
        try {
            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            final DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, secretKey, random);
            return new String(cipher.doFinal(data), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(final String[] args) throws Exception {
        final String enString = encrypt("{\"page\":\"1\",\"limit\":\"10\",\"access_token\":\"a12df572-dcfe-45a7-9b5a-7db6b0ddaeb1\"}");
        System.out.println("加密后的字串是：" + enString);
        final String deString = decrypt(enString);
        System.out.println("解密后的字串是：" + deString);
    }
}
