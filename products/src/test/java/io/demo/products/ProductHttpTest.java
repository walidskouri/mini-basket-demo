package io.demo.products;

import io.demo.products.config.ProductHttpConfig;
import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import org.joda.money.BigMoney;
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
    private ProductRepository repository;

    @Autowired
    private WebTestClient client;

    @Test
    public void getAllProducts() {

        // Mock database Layer
        when(this.repository.findAll())
                .thenReturn(Flux
                        .just(new Product("1", "Savane", "1234564", BigMoney.ofScale(CurrencyUnit.EUR, 120, 2)),
                                new Product("2", "Danette", "1144888", BigMoney.ofScale(CurrencyUnit.EUR, 450, 2)))
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

}
