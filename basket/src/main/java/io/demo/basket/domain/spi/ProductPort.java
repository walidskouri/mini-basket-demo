package io.demo.basket.domain.spi;

import io.demo.basket.domain.model.basket.offer.Product;

import java.util.List;

public interface ProductPort {

    List<Product> searchProducts(List<String> offers);

}
