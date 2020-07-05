package io.demo.basket.domain.model.basket.offer;

import io.demo.basket.domain.service.MoneyUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.BigMoney;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Predicate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Basket implements Serializable {

    private String customerLogin;

    private Integer offersCount;

    private OffsetDateTime creationDate;

    private OffsetDateTime lastModified;

    private List<Offer> offers;

    public BigMoney getTotalOffersMonetaryAmount() {
        return this.getOffers()
                .stream()
                .filter(offer -> validOffer().test(offer))
                .filter(offer -> offer != null && offer.getUnitPrice() != null && offer.getQuantity() != null)
                .map(Offer::getUnitPrice)
                .reduce(BigMoney::plus)
                .orElse(MoneyUtil.unscaledToMoney(0));
    }

    public static Predicate<Offer> validOffer() {
        return hasQuantityHigherThanZero();
    }

    public static Predicate<Offer> hasQuantityHigherThanZero() {
        return offer -> offer.getQuantity() > 0;
    }

}
