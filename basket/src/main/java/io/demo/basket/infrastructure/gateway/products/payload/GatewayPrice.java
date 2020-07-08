package io.demo.basket.infrastructure.gateway.products.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayPrice {

    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("scale")
    private Integer scale;

}
