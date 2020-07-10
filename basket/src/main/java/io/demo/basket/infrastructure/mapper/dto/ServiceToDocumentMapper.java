package io.demo.basket.infrastructure.mapper.dto;

import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.infrastructure.repository.documents.BasketDocument;
import io.demo.basket.infrastructure.repository.documents.MoneyDto;
import io.demo.basket.infrastructure.repository.documents.OfferDto;
import org.joda.money.BigMoney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ServiceToDocumentMapper {


    public abstract BasketDocument toBasketDocument(Basket basket);

    @Mapping(expression = "java(toMoneyDto(offer.getUnitPrice()))", target = "unitPrice")
    public abstract OfferDto toOffer(Offer offer);

    protected List<OfferDto> toOffers(List<Offer> offers) {
        if (null == offers) {
            return null;
        }
        return offers.stream()
                .filter(Offer::isAvailable)
                .map(this::toOffer)
                .collect(Collectors.toList());
    }

    public MoneyDto toMoneyDto(BigMoney bigMoney) {
        if (bigMoney == null) {
            return null;
        }
        return MoneyDto.builder().unscaledAmount(bigMoney.getAmount().unscaledValue().intValue())
                .currency(bigMoney.getCurrencyUnit().getCode())
                .scale(bigMoney.getScale())
                .build();
    }
}
