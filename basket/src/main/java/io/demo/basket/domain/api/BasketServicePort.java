package io.demo.basket.domain.api;


import io.demo.basket.domain.model.basket.Basket;

import java.util.List;

public interface BasketServicePort {

    Basket getBasket(String userLogin);

    Basket addProducts(String userLogin, List<String> productCodes);


}
