package io.demo.basket.infrastructure.gateway.products.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayProduct {

    @JsonProperty("name")
    private String name;
    @JsonProperty("product_code")
    private String productCode;
    @JsonProperty("unit_price")
    private GatewayPrice unitPrice;

}
