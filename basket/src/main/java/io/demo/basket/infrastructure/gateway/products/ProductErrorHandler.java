package io.demo.basket.infrastructure.gateway.products;


import io.demo.basket.domain.exception.ErrorMessageType;
import io.demo.basket.domain.exception.InfrastructureException;
import io.demo.basket.infrastructure.gateway.GatewayErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.demo.basket.infrastructure.gateway.GatewayConstants.PRODUCT_EX_OBJ;

@Component
public class ProductErrorHandler extends GatewayErrorHandler {

    @Override
    public void handleGatewayError(HttpStatus httpStatus, String message) {
        InfrastructureException.InfrastructureExceptionBuilder exceptionBuilder = InfrastructureException.builder()
                .rejectedObjectName(PRODUCT_EX_OBJ)
                .message(message);
        if (httpStatus == HttpStatus.BAD_REQUEST) {
            throw exceptionBuilder.error(ErrorMessageType.SEARCH_PRODUCTS_WITH_PRODUCT_BAD_REQUEST).build().toInfrastructure4xxException();
        }
        throw exceptionBuilder.error(ErrorMessageType.SEARCH_PRODUCTS_WITH_PRODUCT_GENERIC).build();
    }
}
