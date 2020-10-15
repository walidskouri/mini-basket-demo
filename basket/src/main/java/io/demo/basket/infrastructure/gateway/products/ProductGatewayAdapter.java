package io.demo.basket.infrastructure.gateway.products;


import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.domain.spi.ProductPort;
import io.demo.basket.infrastructure.gateway.products.payload.GatewayProduct;
import io.demo.basket.infrastructure.gateway.products.payload.GetProductsDetailsRequest;
import io.demo.basket.infrastructure.mapper.ProductGateWayToServiceMapper;
import io.demo.basket.infrastructure.setting.ProductSettings;
import io.demo.basket.infrastructure.util.logging.CallType;
import io.demo.basket.infrastructure.util.logging.TraceMethodCall;
import io.demo.basket.infrastructure.util.logging.TracingConstant;
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

import static io.demo.basket.domain.exception.ErrorMessageType.SEARCH_PRODUCTS_WITH_PRODUCT_GENERIC;
import static io.demo.basket.infrastructure.gateway.GatewayConstants.PRODUCT_EX_OBJ;
import static io.demo.basket.infrastructure.gateway.GatewayErrorHandler.throwInfrastructureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductGatewayAdapter implements ProductPort {

    private static final String PRODUCTS_SEARCH = "/products/codes";

    private final ProductGateWayToServiceMapper productGateWayToServiceMapper;

    private final ProductSettings productSettings;

    private final RestTemplate productRestTemplate;

    @Override
    @TraceMethodCall(params = "{" + TracingConstant.PRODUCT_CODES + ": #queryOffers}", type = CallType.GATEWAY)
    public List<Product> searchProducts(List<String> queryOffers) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(productSettings.getBaseUri())
                .path(PRODUCTS_SEARCH)
                .build();

        GetProductsDetailsRequest request = GetProductsDetailsRequest
                .builder()
                .productCodes(queryOffers)
                .build();
        ResponseEntity<GatewayProduct[]> listResponseEntity = null;
        try {
            listResponseEntity = productRestTemplate
                    .postForEntity(uriComponents.toUri(), request, GatewayProduct[].class);
        } catch (Exception e) {
            throwInfrastructureException(e, SEARCH_PRODUCTS_WITH_PRODUCT_GENERIC, PRODUCT_EX_OBJ);
        }
        return productGateWayToServiceMapper.toProducts(Stream.of(listResponseEntity.getBody())
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

}
