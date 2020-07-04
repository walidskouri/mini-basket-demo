package io.demo.products;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    public void query() throws Exception {
        // Delete everything in the DB
        // Write 4 records
        // Then query by name and assert count
        Flux<Product> productFlux = this.repository.deleteAll()
                .thenMany(
                        Flux.just("A", "B", "C", "C")
                                .map(name -> new Product(null,
                                        name,
                                        "123454" + name,
                                        BigMoney.ofScale(CurrencyUnit.EUR, 200, 2)))
                                .flatMap(p -> this.repository.save(p))
                ).thenMany(this.repository.findByName("C"));
        StepVerifier
                .create(productFlux)
                .expectNextCount(2)
                .verifyComplete();
    }


}
