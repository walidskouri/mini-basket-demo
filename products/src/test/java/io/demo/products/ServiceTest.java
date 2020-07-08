package io.demo.products;


import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import io.demo.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private ProductRepository repo;


    private ProductService service;

    @Test
    public void testSearchProducts() {

        service = new ProductService(repo);

        Product productA = new Product();
        productA.setId("id-a");
        productA.setName("ProductA");
        productA.setProductCode("code-a");
        when(repo.findByProductCode("A")).thenReturn(Mono.just(productA));
        Product productB = new Product();
        productB.setId("id-b");
        productB.setName("ProductB");
        productB.setProductCode("code-b");
        when(repo.findByProductCode("B")).thenReturn(Mono.just(productB));
        when(repo.findByProductCode("C")).thenReturn(Mono.empty());

        Flux<Product> productFlux = service.getProductsByCodes(Flux.fromStream(List.of("A", "B", "C").stream()));

        StepVerifier
                .create(productFlux)
                .expectNextCount(2)
                .expectComplete()
                .verify();

    }


}
