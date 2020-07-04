package io.demo.products.repository;

import io.demo.products.models.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

    Flux<Product> findByName(String name);

}
