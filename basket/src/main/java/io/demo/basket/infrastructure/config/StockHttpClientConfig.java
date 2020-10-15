package io.demo.basket.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.basket.infrastructure.config.interceptor.HeaderClientHttpRequestInterceptor;
import io.demo.basket.infrastructure.gateway.stock.StockErrorHandler;
import io.demo.basket.infrastructure.util.logging.HttpRequestLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
public class StockHttpClientConfig extends HttpClientConfig {

    private final StockErrorHandler stockErrorHandler;

    public StockHttpClientConfig(StockErrorHandler stockErrorHandler, ObjectMapper objectMapper) {
        super(objectMapper);
        this.stockErrorHandler = stockErrorHandler;
    }

    @Bean
    public RestTemplate productRestTemplate(HttpRequestLoggingInterceptor httpRequestLoggingInterceptor) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.getMessageConverters().addAll(converters());
        restTemplate.setInterceptors(Arrays.asList(new HeaderClientHttpRequestInterceptor(), httpRequestLoggingInterceptor));
        restTemplate.setErrorHandler(stockErrorHandler);
        return restTemplate;
    }

}
