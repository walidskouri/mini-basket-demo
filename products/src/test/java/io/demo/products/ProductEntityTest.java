package io.demo.products;

import com.mongodb.client.result.DeleteResult;
import io.demo.products.models.Price;
import io.demo.products.models.Product;
import org.assertj.core.api.Assertions;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@DataMongoTest
public class ProductEntityTest {

    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @Test
    public void shouldSaveAndGetTheRemove() {
        Product product = new Product(null, "Savane",
                "123465897",
                Price
                        .builder()
                        .currency(CurrencyUnit.EUR.getCode()).amount(12)
                        .scale(2)
                        .build());
        Mono<Product> save = mongoTemplate.save(product);
        StepVerifier
                .create(save)
                .expectNextMatches(createdProduct -> createdProduct.getName().equalsIgnoreCase("Savane")
                        && createdProduct.getProductCode().equalsIgnoreCase("123465897")
                        && createdProduct.getId() != null)
                .verifyComplete();
        DeleteResult remove = mongoTemplate.remove(product).block();
        Assertions.assertThat(remove.getDeletedCount()).isEqualTo(1);
    }


}
