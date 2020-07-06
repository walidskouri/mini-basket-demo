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

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

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
                .map(offer -> offer.getLinePrice(offer.getQuantity()))
                .reduce(BigMoney::plus)
                .orElse(MoneyUtil.unscaledToMoney(0));
    }

    public Integer getTotalOffersCount() {
        return isEmpty(getOffers()) ? 0 : getOffers().size();
    }

    public static Predicate<Offer> validOffer() {
        return hasQuantityHigherThanZero();
    }

    public static Predicate<Offer> hasQuantityHigherThanZero() {
        return offer -> offer.getQuantity() > 0;
    }

}