package com.example.intellipm.security.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Service de chiffrement AES-256-GCM
 * Conforme à la section 6.2 du CDC.
 */
@Service
public class AesEncryptionService {

    private static final String ALGORITHM      = "AES/GCM/NoPadding";
    private static final int    GCM_IV_LENGTH  = 12;  // 96 bits
    private static final int    GCM_TAG_LENGTH = 128; // bits

    @Value("${encryption.secret-key:IntelliPM_AES256_SecretKey_2026!!}")
    private String secretKeyStr;

    private SecretKey getSecretKey() {
        byte[] keyBytes = secretKeyStr.getBytes();
        byte[] key256   = new byte[32]; // 256 bits
        System.arraycopy(keyBytes, 0, key256, 0, Math.min(keyBytes.length, 32));
        return new SecretKeySpec(key256, "AES");
    }

    /**
     * Chiffre une valeur avec AES-256-GCM
     */
    public String encrypt(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[GCM_IV_LENGTH];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), spec);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            // Concaténer IV + données chiffrées puis encoder en Base64
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Erreur de chiffrement AES-256", e);
        }
    }

    /**
     * Déchiffre une valeur chiffrée avec AES-256-GCM
     */
    public String decrypt(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isBlank()) return null;
        try {
            byte[] combined  = Base64.getDecoder().decode(encryptedValue);

            byte[] iv        = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);

        } catch (Exception e) {
            // Si déchiffrement échoue → retourner valeur telle quelle
            // (cas des anciennes données non chiffrées)
            return encryptedValue;
        }
    }
}