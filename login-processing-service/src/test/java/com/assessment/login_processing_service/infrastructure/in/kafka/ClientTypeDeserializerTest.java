package com.assessment.login_processing_service.infrastructure.in.kafka;

import com.assessment.login_processing_service.common.model.ClientType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientTypeDeserializerTest {

    private ClientTypeDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;

    @BeforeEach
    public void setUp() {
        deserializer = new ClientTypeDeserializer();
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
    }

    @Test
    void deserializeWeb() throws Exception {
        when(parser.getText()).thenReturn("web");
        ClientType result = deserializer.deserialize(parser, context);
        assertEquals(ClientType.WEB, result);
    }

    @Test
    void deserializeAndroid() throws Exception {
        when(parser.getText()).thenReturn("ANDROID");
        ClientType result = deserializer.deserialize(parser, context);
        assertEquals(ClientType.ANDROID, result);
    }

    @Test
    void deserializeIosMixedCase() throws Exception {
        when(parser.getText()).thenReturn("IoS");
        ClientType result = deserializer.deserialize(parser, context);
        assertEquals(ClientType.IOS, result);
    }

    @Test
    void deserializeInvalidValue() {
        assertThrows(IOException.class, () -> {
            when(parser.getText()).thenReturn("desktop");
            deserializer.deserialize(parser, context);
        });
    }
}