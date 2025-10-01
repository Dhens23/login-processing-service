package com.assessment.login_processing_service.infrastructure.in.kafka;

import com.assessment.login_processing_service.common.model.ClientType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientTypeDeserializerTest {

    @Mock
    private JsonParser parser;
    @Mock
    private DeserializationContext context;
    @InjectMocks
    private ClientTypeDeserializer deserializer;

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