package io.demo.basket.infrastructure.repository;


import io.demo.basket.infrastructure.repository.documents.BasketDocument;

import java.util.Optional;

public interface BasketRepository {

    Optional<BasketDocument> findById(String userLogin);

    BasketDocument save(BasketDocument oneBasketDto);

}
