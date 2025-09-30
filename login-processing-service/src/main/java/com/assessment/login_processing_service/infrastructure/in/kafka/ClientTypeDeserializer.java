package com.assessment.login_processing_service.infrastructure.in.kafka;

import com.assessment.login_processing_service.common.model.ClientType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ClientTypeDeserializer extends JsonDeserializer<ClientType> {

    @Override
    public ClientType deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        String value = parser.getText().toUpperCase();
        try {
            return ClientType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid clientType value: " + value);
        }
    }
}