package io.demo.basket.infrastructure.mapper.dto;

import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.service.MoneyUtil;
import io.demo.basket.infrastructure.repository.documents.BasketDocument;
import io.demo.basket.infrastructure.repository.documents.MoneyDto;
import io.demo.basket.infrastructure.repository.documents.OfferDto;
import org.joda.money.BigMoney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class DocumentToServiceMapper {

    public abstract Basket documentToServiceBasket(BasketDocument basketDocument);

    @Mapping(expression = "java(dtoToServiceMoney(offerDto.getUnitPrice()))", target = "unitPrice")
    @Mapping(source = "productCode", target = "id")
    public abstract Offer toOffer(OfferDto offerDto);

    public BigMoney dtoToServiceMoney(MoneyDto moneyDto) {
        if (moneyDto == null || moneyDto.getUnscaledAmount() == null) return null;
        else
            return MoneyUtil.unscaledToMoney(moneyDto.getUnscaledAmount(), moneyDto.getScale());
    }


}
