package io.demo.basket.domain.service;


import io.demo.basket.domain.api.BasketServicePort;
import io.demo.basket.domain.model.basket.offer.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.BigMoney;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class BasketService implements BasketServicePort {

    @Override
    public Basket getBasket(String userLogin) {
        Basket dummyBasket = new Basket();
        dummyBasket.setCreationDate(OffsetDateTime.now());
        dummyBasket.setLastModified(OffsetDateTime.now());
        dummyBasket.setCustomerLogin(userLogin);
        dummyBasket.setOffers(createDummyOffers());
        dummyBasket.setOffersCount(dummyBasket.getOffers().size());
        return dummyBasket;
    }

    private List<Offer> createDummyOffers() {
        List<Offer> offers = new ArrayList<>();
        for (int i = 1; i < new Random().nextInt(50) + 1; i++) {
            offers.add(dummyOffer("Offer" + i));
        }
        return offers;
    }

    private Offer dummyOffer(String name) {
        return Offer
                .builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .quantity(new Random().nextInt(12) + 1)
                .unitPrice(createDummyPrice())
                .build();
    }

    private BigMoney createDummyPrice() {
        return MoneyUtil
                .unscaledToMoney(new Random().nextInt(300) + 100);
    }
}
