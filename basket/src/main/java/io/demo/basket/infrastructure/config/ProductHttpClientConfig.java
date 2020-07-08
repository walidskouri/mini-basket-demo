package io.demo.basket.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.basket.infrastructure.gateway.products.ProductErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductHttpClientConfig extends HttpClientConfig {

    private final ProductErrorHandler productErrorHandler;

    public ProductHttpClientConfig(ProductErrorHandler productErrorHandler, ObjectMapper objectMapper) {
        super(objectMapper);
        this.productErrorHandler = productErrorHandler;
    }

    @Bean
    public RestTemplate productRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(converters());
        restTemplate.setErrorHandler(productErrorHandler);
        return restTemplate;
    }

}
