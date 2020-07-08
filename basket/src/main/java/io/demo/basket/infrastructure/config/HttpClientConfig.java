package io.demo.basket.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HttpClientConfig {

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private final ObjectMapper objectMapper;

    public HttpClientConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<HttpMessageConverter<?>> converters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(getMappingJackson2HttpMessageConverter());
        return messageConverters;
    }

    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(getJacksonObjectMapper());
        jsonConverter.setSupportedMediaTypes(Collections.singletonList(contentType));
        return jsonConverter;
    }

    public ObjectMapper getJacksonObjectMapper() {
        return objectMapper;
    }
}