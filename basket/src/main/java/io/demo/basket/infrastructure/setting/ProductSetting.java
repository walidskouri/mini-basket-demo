package io.demo.basket.infrastructure.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = ProductSetting.PRODUCT_PREFIX)
public class ProductSetting {

    public static final String PRODUCT_PREFIX = "products";

    @NotBlank
    private String baseUri;

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
