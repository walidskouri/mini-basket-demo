package io.demo.products.config;

import io.demo.products.models.Product;
import io.demo.products.models.request.GetProductsDetailsRequest;
import io.demo.products.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class ProductHttpConfig {

    @Bean
    RouterFunction<ServerResponse> responseRouterFunction(ProductService service) {
        return route()
                .GET("/products",
                        serverRequest -> ServerResponse
                                .ok()
                                .body(service.all().log(), Product.class))
                .GET("/products/{name}",
                        serverRequest -> ServerResponse
                                .ok()
                                .body(service.byNameLike(serverRequest.pathVariable("name")).log(), Product.class))
                .POST("/products/codes", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service
                                        .getProductsByCodes(serverRequest
                                                .bodyToMono(GetProductsDetailsRequest.class)
                                                .flatMapMany(this::fromRequest))
                                , Product.class))
                .build();
    }

    private Flux<String> fromRequest(GetProductsDetailsRequest getProductsDetailsRequest) {
        log.info("Request : " + getProductsDetailsRequest);
        return Flux.fromStream(getProductsDetailsRequest.getProductCodes().stream());
    }

}
