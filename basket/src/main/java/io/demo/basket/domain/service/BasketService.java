package io.demo.basket.domain.service;


import io.demo.basket.domain.api.BasketServicePort;
import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.domain.spi.ProductPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.money.BigMoney;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService implements BasketServicePort {

    private final ProductPort productPort;

    @Override
    public Basket getBasket(String userLogin) {
        Basket dummyBasket = new Basket();
        dummyBasket.setCreationDate(OffsetDateTime.now());
        dummyBasket.setLastModified(OffsetDateTime.now());
        dummyBasket.setCustomerLogin(userLogin);
        dummyBasket.setOffers(createDummyOffers());
        return dummyBasket;
    }

    @Override
    public Basket addProducts(String userLogin, List<String> productCodes) {
        Basket dummyBasket = getBasket(userLogin);
        List<Offer> newOffers = toOffers(productPort.searchProducts(concatAllOfferCodesToQuery(dummyBasket.getOffers(), productCodes)));
        List<Offer> consolidated = consolidateNotFoundOffers(newOffers, productCodes);
        dummyBasket.getOffers().addAll(consolidated);
        dummyBasket.setOffers(completeOffers(newOffers, dummyBasket.getOffers()));
        return dummyBasket;
    }

    private List<String> concatAllOfferCodesToQuery(List<Offer> offers, List<String> productCodes) {
        List<String> alreadyInBasketCodes = offers.stream().map(Offer::getId).filter(code -> !productCodes.contains(code)).collect(toList());
        if (CollectionUtils.isNotEmpty(alreadyInBasketCodes)) {
            return Stream.concat(productCodes.stream(), alreadyInBasketCodes.stream()).collect(toList());
        }
        return productCodes;
    }

    private List<Offer> consolidateNotFoundOffers(List<Offer> newOffers, List<String> productCodes) {
        List<Offer> notReturnedFromProduct = buildNotReturnedOffers(newOffers.stream().map(Offer::getId).collect(Collectors.toList()), productCodes);
        return Stream.concat(newOffers.stream(), notReturnedFromProduct.stream()).collect(Collectors.toList());
    }

    private List<Offer> buildNotReturnedOffers(List<String> returnedOffers, List<String> productCodes) {
        return productCodes.stream().filter(requestedCode -> !returnedOffers.contains(requestedCode)).map(this::toNotFound).collect(Collectors.toList());
    }

    private Offer toNotFound(String code) {
        return Offer.builder().id(code).available(false).quantity(0).build();
    }

    public List<Offer> completeOffers(List<Offer> offersToAdd, List<Offer> offersInBasket) {
        List<Offer> allOffers = completeOrReplaceExistingOffersWithOffersToAdd(offersInBasket, offersToAdd);
        if (isEmpty(allOffers)) {
            return emptyList();
        }
        return allOffers
                .stream()
                .map(Offer.completeOfferWithProductInfo(offersToAdd))
                .collect(Collectors.toList());
    }


    private List<Offer> completeOrReplaceExistingOffersWithOffersToAdd(List<Offer> offersToAdd, List<Offer> offersInBasket) {
        Set<String> offersToAddIds = offersToAdd.stream()
                .map(Offer::getId)
                .collect(toSet());

        List<Offer> existingOffersWithoutOnesToAdd = offersInBasket.stream()
                .filter(offer -> !offersToAddIds.contains(offer.getId()))
                .collect(toList());

        return Stream.concat(existingOffersWithoutOnesToAdd.stream(), offersToAdd.stream()).collect(toList());

//        return offersInBasket
//                .stream()
//                .map(offer -> Offer.completeOfferWithProductInfo(offersToAdd).apply(offer))
//                .collect(Collectors.toList());
    }

    private List<Offer> toOffers(List<Product> foundProducts) {
        return foundProducts.stream().map(this::mapProduct).collect(Collectors.toList());
    }

    private Offer mapProduct(Product product) {
        return Offer
                .builder()
                .unitPrice(product.getUnitPrice())
                .quantity(new Random().nextInt(10) + 1)
                .name(product.getName())
                .available(true)
                .id(product.getProductCode())
                .build();
    }

    private List<Offer> createDummyOffers() {
        List<Offer> offers = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(2); i++) {
            offers.add(dummyOffer("Offer" + i + 1));
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
