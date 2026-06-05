package com.example.intellipm.security.encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Converter JPA — chiffre automatiquement les champs annotés @Convert
 * avant le stockage et les déchiffre à la lecture.
 * Conforme à la section 6.2 du CDC.
 */
@Converter
@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static AesEncryptionService encryptionService;

    @Autowired
    public void setEncryptionService(AesEncryptionService service) {
        AttributeEncryptor.encryptionService = service;
    }

    /**
     * Appelé AVANT le stockage en BDD → chiffre la valeur
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (encryptionService == null || attribute == null) return attribute;
        return encryptionService.encrypt(attribute);
    }

    /**
     * Appelé APRÈS la lecture depuis la BDD → déchiffre la valeur
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (encryptionService == null || dbData == null) return dbData;
        return encryptionService.decrypt(dbData);
    }
}