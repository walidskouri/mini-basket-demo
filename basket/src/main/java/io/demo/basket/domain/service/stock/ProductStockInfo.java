package io.demo.basket.domain.service.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockInfo {

    private String productCode;
    private Boolean isProductAvailable;
    private Integer quantityAvailable;
}
