package io.demo.products;

import com.mongodb.client.result.DeleteResult;
import io.demo.products.models.Product;
import org.assertj.core.api.Assertions;
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
        Product product = new Product(null, "Savane");
        Mono<Product> save = mongoTemplate.save( product);
        StepVerifier
                .create(save)
                .expectNextMatches(createdProduct -> createdProduct.getName().equalsIgnoreCase("Savane") && createdProduct.getId() != null)
                .verifyComplete();
        DeleteResult remove = mongoTemplate.remove(product).block();
        Assertions.assertThat(remove.getDeletedCount()).isEqualTo(1);
    }


}
