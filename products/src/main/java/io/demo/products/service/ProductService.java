package io.demo.products.service;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final ProductRepository repository;

    public Flux<Product> all() {
        return repository.findAll().timeout(Duration.ofSeconds(5)).retry(5);
    }

    public Flux<Product> byNameLike(String nameQuery) {
        return repository.findByNameRegexp(nameQuery);
    }

    public Flux<Product> getProductsByCodes(Flux<String> productCodes) {
        return productCodes.flatMap(repository::findByProductCode);
    }

}
