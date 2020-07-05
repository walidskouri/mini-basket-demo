package io.demo.basket.infrastructure.gateway.products.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String id;
    private String name;
    private String productCode;
    private Price unitPrice;

}
