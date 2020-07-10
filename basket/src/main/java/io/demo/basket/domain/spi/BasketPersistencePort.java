package io.demo.basket.domain.spi;


import io.demo.basket.domain.model.basket.Basket;

import java.util.Optional;

public interface BasketPersistencePort {

    Optional<Basket> getBasket(String userLogin);

    Basket saveBasket(Basket basket, String userLogin);

}
