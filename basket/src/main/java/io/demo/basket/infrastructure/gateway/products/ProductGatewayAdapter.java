package io.demo.basket.infrastructure.gateway.products;


import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.domain.spi.ProductPort;
import io.demo.basket.infrastructure.gateway.products.payload.GatewayProduct;
import io.demo.basket.infrastructure.gateway.products.payload.GetProductsDetailsRequest;
import io.demo.basket.infrastructure.mapper.ProductGateWayToServiceMapper;
import io.demo.basket.infrastructure.setting.ProductSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.demo.basket.infrastructure.gateway.GatewayErrorHandler.toInfrastructureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductGatewayAdapter implements ProductPort {

    private static final String PRODUCTS_SEARCH = "/products/codes";

    private final ProductGateWayToServiceMapper productGateWayToServiceMapper;

    private final ProductSetting productSetting;

    private final RestTemplate productRestTemplate;

    @Override
    public List<Product> searchProducts(List<String> queryOffers) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(productSetting.getBaseUri())
                .path(PRODUCTS_SEARCH)
                .build();

        GetProductsDetailsRequest request = GetProductsDetailsRequest
                .builder()
                .productCodes(queryOffers)
                .build();
        try {
            ResponseEntity<GatewayProduct[]> listResponseEntity = productRestTemplate
                    .postForEntity(uriComponents.toUri(), request, GatewayProduct[].class);
            return productGateWayToServiceMapper.toProducts(Stream.of(listResponseEntity.getBody())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            throw toInfrastructureException(e);
        }
    }

}
