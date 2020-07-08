package io.demo.basket.domain.spi;

import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.infrastructure.gateway.products.payload.GatewayProduct;

import java.util.List;

public interface ProductPort {

    List<Product> searchProducts(List<String> offers);

}
