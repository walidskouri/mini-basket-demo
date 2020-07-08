package io.demo.basket.domain.model.basket.offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.joda.money.BigMoney;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String name;
    private String productCode;
    private BigMoney unitPrice;

}
