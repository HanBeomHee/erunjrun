package com.erunjrun.config;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class AesConfig {

	// 기본적으로 사용할 암호화 키를 생성
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "1234567890123456"; // 16바이트 키 (128 비트)

    // AES 암호화
    public static String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.encodeBase64String(encrypted); // Base64로 인코딩
    }

    // AES 복호화
    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedData = Base64.decodeBase64(encryptedData); // Base64로 디코딩
        byte[] original = cipher.doFinal(decodedData);
        return new String(original);
    }

    // 키 생성 (여기서는 예시로 키를 고정값으로 설정)
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128); // 128 비트 키
        return keyGenerator.generateKey();
    }

}