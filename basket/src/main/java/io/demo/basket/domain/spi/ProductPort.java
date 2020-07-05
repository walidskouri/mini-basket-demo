package io.demo.basket.domain.spi;


import io.demo.basket.domain.model.basket.offer.Offer;

import java.util.List;

public interface ProductPort {

    List<Offer> searchProducts(List<Offer> offers);

}
