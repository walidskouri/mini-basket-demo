package io.demo.products.service;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final Tracer tracer;
    private final ProductRepository repository;

    public Flux<Product> all() {

        tracer.activeSpan().log("Looking for all products");
        return repository.findAll();
    }

    public Flux<Product> byNameLike(String nameQuery) {

        tracer.activeSpan().log("Looking for products with name containing '" + nameQuery + "'");
        return repository.findByNameRegexp(nameQuery);
    }


    public Flux<Product> getProductsByCodes(Flux<String> productCodes) {
        tracer.activeSpan().log("Getting Products By Codes");
        return productCodes.flatMap(repository::findByProductCode);
    }


}
