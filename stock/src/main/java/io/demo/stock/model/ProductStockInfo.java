package io.demo.stock.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockInfo {

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("is_product_available")
    private Boolean isProductAvailable;

    @JsonProperty("quantity_available")
    private Integer quantityAvailable;

}
