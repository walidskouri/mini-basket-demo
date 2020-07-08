package io.demo.products.repository;

import io.demo.products.models.Product;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

    @Query("{ 'name' : { '$regex' : ?0 , $options: 'i'}}")
    Flux<Product> findByNameRegexp(String name);

    Mono<Product> findByProductCode(String productCode);

}
