package com.bancode.sangue.api.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }
        return CryptoUtil.encrypt(attribute); // Criptografa os dados antes de salvar no banco
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }
        return CryptoUtil.decrypt(dbData); // Descriptografa os dados ao recuperar do banco
    }
}