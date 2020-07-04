package io.demo.products.config;

import io.demo.products.models.Product;
import io.demo.products.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductHttpConfig {

    @Bean
    RouterFunction<ServerResponse> responseRouterFunction(ProductRepository productRepository) {
        return route()
                .GET("/products", serverRequest -> ServerResponse.ok().body(productRepository.findAll(), Product.class))
                .build();
    }

}
