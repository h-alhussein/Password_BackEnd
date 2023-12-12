package com.thkoeln.passwordskey.account.application;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class AESService {

    String algorithm="AES/CBC/PKCS5Padding";
    String password = "password";
    String salt = "salt";
    IvParameterSpec iv = generateIv();
    SecretKey key = getKeyFromPassword(password,salt);

    @SneakyThrows
    public static SecretKey getKeyFromPassword(String password, String salt) {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = {1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0};
       // byte[] iv = new byte[16];
       // new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    @SneakyThrows
    public  String encrypt( String input) {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }


    @SneakyThrows
    public  String decrypt( String cipherText)  {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }

}
