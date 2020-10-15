package io.demo.basket.infrastructure.gateway.stock.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayProductStockInfo {

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("is_product_available")
    private Boolean isProductAvailable;

    @JsonProperty("quantity_available")
    private Integer quantityAvailable;

}
