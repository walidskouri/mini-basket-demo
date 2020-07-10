package io.demo.basket.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.basket.infrastructure.gateway.products.ProductErrorHandler;
import io.demo.basket.infrastructure.util.logging.HttpRequestLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class ProductHttpClientConfig extends HttpClientConfig {

    private final ProductErrorHandler productErrorHandler;

    public ProductHttpClientConfig(ProductErrorHandler productErrorHandler, ObjectMapper objectMapper) {
        super(objectMapper);
        this.productErrorHandler = productErrorHandler;
    }

    @Bean
    public RestTemplate productRestTemplate(HttpRequestLoggingInterceptor httpRequestLoggingInterceptor) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.getMessageConverters().addAll(converters());
        restTemplate.setInterceptors(Collections.singletonList(httpRequestLoggingInterceptor));
        restTemplate.setErrorHandler(productErrorHandler);
        return restTemplate;
    }

}
