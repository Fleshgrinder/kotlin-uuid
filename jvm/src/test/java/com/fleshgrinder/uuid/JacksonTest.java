package com.fleshgrinder.uuid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class JacksonTest {
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    private static class Container {
        @JsonProperty
        private final Uuid id = new Uuid();
    }

    @Test
    void simple_json_serialization() throws JsonProcessingException {
        @Language("JSON") final String expected = "\"00000000-0000-0000-0000-000000000000\"";
        final String actual = jsonMapper.writeValueAsString(new Uuid());

        assertEquals(expected, actual);
    }

    @Test
    void simple_json_deserialization() throws JsonProcessingException {
        final Uuid expected = new Uuid();
        @Language("JSON") final String content = "\"00000000-0000-0000-0000-000000000000\"";
        final Uuid actual = jsonMapper.readValue(content, Uuid.class);

        assertEquals(expected, actual);
    }

    @Test
    void complex_json_serialization() throws JsonProcessingException {
        @Language("JSON") final String expected = "{\"id\":\"00000000-0000-0000-0000-000000000000\"}";
        final String actual = jsonMapper.writeValueAsString(new Container());

        assertEquals(expected, actual);
    }

    @Test
    void complex_json_deserialization() throws JsonProcessingException {
        final Container expected = new Container();
        @Language("JSON") final String content = "{\"id\":\"00000000-0000-0000-0000-000000000000\"}";
        final Container actual = jsonMapper.readValue(content, Container.class);

        assertEquals(expected.id, actual.id);
    }
}
