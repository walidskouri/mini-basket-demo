package io.demo.products.service;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<Product> all() {
        return repository.findAll();
    }

    public Flux<Product> byNameLike(String nameQuery) {
        return repository.findByNameRegexp(nameQuery);
    }


}
