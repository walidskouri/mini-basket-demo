package io.demo.basket.domain.api;


import io.demo.basket.domain.model.basket.offer.Basket;

public interface BasketServicePort {

    Basket getBasket(String userLogin);

}
