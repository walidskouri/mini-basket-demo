package io.demo.products;

import io.demo.products.config.ProductHttpConfig;
import io.demo.products.models.Price;
import io.demo.products.models.Product;
import io.demo.products.service.ProductService;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(value = ProductHttpConfig.class)
public class ProductHttpTest {

    @MockBean
    private ProductService service;


    @Autowired
    private WebTestClient client;

    @Test
    public void getAllProducts() {

        // Mock Service Layer
        when(this.service.all())
                .thenReturn(Flux
                        .just(new Product("1", "Savane", "1234564", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(120).scale(2).build()),
                                new Product("2", "Danette", "1144888", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(450).scale(2).build()))
                );
        this.client
                .get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("@.[0].name").isEqualTo("Savane")
                .jsonPath("@.[1].name").isEqualTo("Danette");
    }

    @Test
    public void queryProducts() {
        // Mock Service Layer
        when(this.service.byNameLike("ham"))
                .thenReturn(Flux
                        .just(new Product("1", "Graham Cracker Mix", "1234564", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(120).scale(2).build()),
                                new Product("2", "Ham Black Forest", "1144888", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(450).scale(2).build()))
                );
        this.client
                .get()
                .uri("/products/ham")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("@.[0].name").isEqualTo("Graham Cracker Mix")
                .jsonPath("@.[1].name").isEqualTo("Ham Black Forest");
    }


}
