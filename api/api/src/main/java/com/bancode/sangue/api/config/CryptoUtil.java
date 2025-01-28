package com.bancode.sangue.api.config;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {

    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // Tamanho do Initialization Vector (IV) - 12 bytes
    private static final int GCM_TAG_LENGTH = 128; // Tamanho do Authentication Tag - 128 bits
    private static final String SECRET_KEY = "chave-super-segura-de-32-chars!!"; // Exatamente 32 caracteres

    public static String encrypt(String data) {
        try {
            byte[] iv = generateIV(); // Gerar um IV aleatório
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Concatena o IV com os dados criptografados
            byte[] encryptedWithIV = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIV, 0, iv.length);
            System.arraycopy(encryptedData, 0, encryptedWithIV, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedWithIV); // Retorna como Base64
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar os dados", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            byte[] encryptedWithIV = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encryptedBytes = new byte[encryptedWithIV.length - GCM_IV_LENGTH];

            // Separar o IV dos dados criptografados
            System.arraycopy(encryptedWithIV, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedWithIV, GCM_IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] decryptedData = cipher.doFinal(encryptedBytes);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar os dados", e);
        }
    }

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES);
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}