package io.demo.basket.infrastructure.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = StockSettings.STOCK_PREFIX)
public class StockSettings {

    public static final String STOCK_PREFIX = "stock";

    @NotBlank
    private String baseUri;

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
