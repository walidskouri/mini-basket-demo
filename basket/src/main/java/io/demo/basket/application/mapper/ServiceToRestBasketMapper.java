package io.demo.basket.application.mapper;

import io.demo.basket.application.rest.response.RestBasketResponse;
import io.demo.basket.application.rest.response.RestMoney;
import io.demo.basket.application.rest.response.RestOffer;
import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import org.joda.money.BigMoney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ServiceToRestBasketMapper {


    public abstract RestBasketResponse toTeRestBasketResponse(Basket basket);

    @Mapping(expression = "java(toRestMoney(offer.getLinePrice(offer.getQuantity())))", target = "linePrice")
    @Mapping(source = "id", target = "productCode")
    protected abstract RestOffer offerToRestOffer(Offer offer);

    public abstract List<RestOffer> toOffers(List<Offer> offers);

    public RestMoney toRestMoney(BigMoney money) {
        if (money != null) {
            RestMoney restMoney = new RestMoney();
            restMoney.setScale(money.getScale());
            restMoney.setCurrency(money.getCurrencyUnit().getCode());
            restMoney.setUnscaledAmount(money.getAmount().unscaledValue().intValue());
            return restMoney;
        } else return null;
    }

}
