package io.demo.basket.infrastructure.repository;

import io.demo.basket.infrastructure.repository.documents.BasketDocument;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepositoryDelegate extends CrudRepository<BasketDocument, String> {
}
