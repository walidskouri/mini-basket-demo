package io.demo.basket.infrastructure.gateway.products;


import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.spi.ProductPort;
import io.demo.basket.infrastructure.gateway.products.payload.Price;
import io.demo.basket.infrastructure.gateway.products.payload.Product;
import lombok.RequiredArgsConstructor;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static org.joda.money.BigMoney.ofScale;

@Component
@RequiredArgsConstructor
public class ProductGatewayAdapter implements ProductPort {

    private static final int SCALE_EURO = 2;

    private static final String PRODUCTS_SEARCH = "/products";

    private final WebClient productWebClient;

    @Override
    public List<Offer> searchProducts(List<Offer> queryOffers) {
        List<Product> productFlux = productWebClient.get().uri(uriBuilder -> uriBuilder.path(PRODUCTS_SEARCH).build())
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();
        assert productFlux != null;
        return productFlux.stream().map(this::transformToOffer).collect(Collectors.toList());
    }

    private Offer transformToOffer(Product product) {
        return Offer
                .builder()
                .id(product.getProductCode())
                .name(product.getName())
                .quantity(1)
                .unitPrice(toMoney(product.getUnitPrice()))
                .build();
    }

    private BigMoney toMoney(Price unitPrice) {
        return ofScale(CurrencyUnit.EUR,
                unitPrice.getAmount(),
                unitPrice.getScale())
                .withScale(SCALE_EURO, RoundingMode.HALF_UP);
    }
}
