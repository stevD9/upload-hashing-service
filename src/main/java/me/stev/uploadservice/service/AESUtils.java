package me.stev.uploadservice.service;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static SecretKey getKey(String keyEncoded) {
        return new SecretKeySpec(Base64Utils.decodeFromString(keyEncoded), "AES");
    }

    public static IvParameterSpec getIV(String ivEncoded) {
        return new IvParameterSpec(Base64Utils.decodeFromString(ivEncoded));
    }

    public static String encrypt(byte[] hash, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(hash);
        return Hex.toHexString(cipherText);
    }

    public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] hash = cipher.doFinal(Hex.decode(cipherText));
        return Hex.toHexString(hash);
    }

}
