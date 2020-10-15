package io.demo.basket.domain.service;


import io.demo.basket.domain.api.BasketServicePort;
import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.domain.service.stock.ProductStockInfo;
import io.demo.basket.domain.spi.BasketPersistencePort;
import io.demo.basket.domain.spi.ProductPort;
import io.demo.basket.domain.spi.StockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.demo.basket.domain.model.basket.offer.Offer.completeOfferWithProductInfo;
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
    private final StockPort stockPort;

    private static final int STOCK_FUTURE_TIMEOUT_IN_SECONDS = 10;


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
        basket.setOffers(completeOffers(newOffers, basket.getOffers().stream().filter(offer -> offer.getQuantity() > 0).collect(Collectors.toList())));
        basket.setLastModified(OffsetDateTime.now());
        basketPersistencePort.saveBasket(basket, userLogin);
        return basket;
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
        return Stream.concat(newOffers.stream(), notReturnedFromProduct.stream().filter(offer -> offer.getQuantity() > 0)).collect(Collectors.toList());
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

        List<String> productCodes = allOffers.stream().map(Offer::getId).filter(Objects::nonNull).collect(toList());


        List<ProductStockInfo> offersStockInfoList = getProductsAvailabilityInfoFutures(productCodes);

        return allOffers
                .stream()
                .map(completeOfferWithProductInfo(offersToAdd))
                .map(completeOfferWithStockInfo(offersStockInfoList))
                .collect(Collectors.toList());
    }


    private List<ProductStockInfo> getProductsAvailabilityInfoFutures(List<String> productCodes) {
        return productCodes.stream().map(stockPort::getProductAvailabilityInfo).collect(Collectors.toList());
    }

    public static Function<Offer, Offer> completeOfferWithStockInfo(List<ProductStockInfo> productStockInfoList) {
        return offer -> {
            Offer.OfferBuilder<?, ?> builder = offer.toBuilder();
            Optional<ProductStockInfo> productStockInfoMatchingOffer = productStockInfoList.stream()
                    .filter(Objects::nonNull)
                    .filter(productStockInfo -> productStockInfo.getProductCode().equals(offer.getId()))
                    .findFirst();
            if (productStockInfoMatchingOffer.isPresent()) {
                ProductStockInfo productStockInfo = productStockInfoMatchingOffer.get();
                if (offer.isAvailable()) {
                    builder.available(BooleanUtils.toBoolean(productStockInfo.getIsProductAvailable()));
                    builder.quantityAvailable(productStockInfo.getQuantityAvailable());
                }
            }
            return builder.build();
        };
    }

    private List<Offer> completeOrReplaceExistingOffersWithOffersToAdd(List<Offer> offersInBasket, List<Offer> offersToAdd) {

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
