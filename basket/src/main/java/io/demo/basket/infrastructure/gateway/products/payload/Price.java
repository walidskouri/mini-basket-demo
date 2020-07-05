package io.demo.basket.infrastructure.gateway.products.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {

    private Integer amount;
    private String currency;
    private Integer scale;

}
