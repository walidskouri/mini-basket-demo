package io.demo.products.config;

import io.demo.products.models.Product;
import io.demo.products.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductHttpConfig {

    @Bean
    RouterFunction<ServerResponse> responseRouterFunction(ProductService service) {
        return route()
                .GET("/products",
                        serverRequest -> ServerResponse
                                .ok()
                                .body(service.all(), Product.class))
                .GET("/products/{name}",
                        serverRequest -> ServerResponse
                                .ok()
                                .body(service.byNameLike(serverRequest.pathVariable("name")), Product.class))
                .build();
    }

}
