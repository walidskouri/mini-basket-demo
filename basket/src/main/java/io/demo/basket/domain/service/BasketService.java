package io.demo.basket.domain.service;


import io.demo.basket.domain.api.BasketServicePort;
import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.domain.spi.BasketPersistencePort;
import io.demo.basket.domain.spi.ProductPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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

    private final BasketPersistencePort basketPersistencePort;
    private final ProductPort productPort;


    @Override
    public Basket getBasket(String userLogin) {
        Optional<Basket> optionalBasket = basketPersistencePort.getBasket(userLogin);
        return optionalBasket.orElseGet(() -> basketPersistencePort
                .saveBasket(Basket.builder().customerLogin(userLogin).offersCount(0).lastModified(OffsetDateTime.now()).creationDate(OffsetDateTime.now()).build(), userLogin));
    }

    @Override
    public Basket addProducts(String userLogin, List<String> productCodes) {
        Basket basket = getBasket(userLogin);
        List<Offer> newOffers = toOffers(productPort.searchProducts(concatAllOfferCodesToQuery(basket.getOffers(), productCodes)));
        List<Offer> consolidated = consolidateNotFoundOffers(newOffers, productCodes);
        basket.getOffers().addAll(consolidated);
        basket.setOffers(completeOffers(newOffers, basket.getOffers()));
        basket.setLastModified(OffsetDateTime.now());
        return basketPersistencePort.saveBasket(basket, userLogin);
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

}
