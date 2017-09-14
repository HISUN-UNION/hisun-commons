package com.hisun.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jamin30 on 2015/12/4. pm
 */
public class AesUntil {

    private static final Logger logger = Logger.getLogger(AesUntil.class);

    private static IvParameterSpec ivSpec;
    private static SecretKeySpec keySpec;

    public static byte[] encrypt(byte[] origData,String key) {
        try {
            initSecretValue(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(origData);
        }  catch (Exception e) {
            logger.error(e,e);
        }
        return null;
    }

    public static byte[] decrypt(byte[] crypted,String key) {
        try {
            initSecretValue(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(crypted);
        }  catch (Exception e) {
            logger.error(e,e);
        }
        return null;
    }

    public static String base64Encode(byte[] data) {
        byte[] enbytes = new Base64().encodeBase64Chunked(data);
        return new String(enbytes);
    }

    public static byte[] baseDecode(byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    public static void initSecretValue(String key)
    {
        try {
            byte[] keyBytes = key.getBytes();
            byte[] buf = new byte[16];

            for (int i = 0; i < keyBytes.length && i < buf.length; i++) {
                buf[i] = keyBytes[i];
            }

            keySpec = new SecretKeySpec(buf, "AES");
            ivSpec = new IvParameterSpec(keyBytes);
        }  catch (Exception e) {
            logger.error(e,e);
        }
    }

}
