package io.demo.basket.domain.model.basket.offer;

import io.demo.basket.domain.service.MoneyUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.joda.money.BigMoney;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Offer implements Serializable {

    @Positive
    private Integer quantity = null;

    private String id = null;

    private String name;

    private BigMoney unitPrice;

    private boolean available = true;

    // stock
    private Integer quantityAvailable;

    public BigMoney getLinePrice(Integer quantity) {
        BigMoney computedLinePrice = MoneyUtil.unscaledToMoney(0);
        if (unitPrice != null && quantity >= 0 && unitPrice.isPositiveOrZero()) {
            computedLinePrice = unitPrice.multipliedBy(quantity);
        }
        return computedLinePrice;
    }

    public static Function<Offer, Offer> completeOfferWithProductInfo(List<Offer> foundProductCodes) {
        return offer ->
                foundProductCodes.stream().filter(product -> product.getId().equalsIgnoreCase(offer.getId()))
                        .findFirst()
                        .orElse(offer.toBuilder().available(false).quantity(0).build());
    }

}
