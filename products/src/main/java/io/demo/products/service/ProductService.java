package io.demo.products.service;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductService {
p
    private final ProductRepository repository;

    public Flux<Product> all() {
        return repository.findAll();
    }

}
