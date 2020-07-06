package io.demo.basket.domain.model.basket.offer;

import io.demo.basket.domain.service.MoneyUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.BigMoney;

import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Offer implements Serializable {

    @Positive
    private Integer quantity = null;

    private String id = null;

    private String name;

    private BigMoney unitPrice;

    public BigMoney getLinePrice(Integer quantity) {
        BigMoney computedLinePrice = MoneyUtil.unscaledToMoney(0);
        if (unitPrice != null && quantity >= 0 && unitPrice.isPositiveOrZero()) {
            computedLinePrice = unitPrice.multipliedBy(quantity);
        }
        return computedLinePrice;
    }

}