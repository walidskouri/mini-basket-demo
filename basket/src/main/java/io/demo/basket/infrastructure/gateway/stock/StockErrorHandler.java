package io.demo.basket.infrastructure.gateway.stock;

import io.demo.basket.domain.exception.ErrorMessageType;
import io.demo.basket.domain.exception.InfrastructureException;
import io.demo.basket.infrastructure.gateway.GatewayErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.demo.basket.infrastructure.gateway.GatewayConstants.STOCK_EX_OBJ;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Component
public class StockErrorHandler extends GatewayErrorHandler {

    @Override
    public void handleGatewayError(HttpStatus httpStatus, String message) {
        InfrastructureException.InfrastructureExceptionBuilder exceptionBuilder = InfrastructureException.builder()
                .rejectedObjectName(STOCK_EX_OBJ)
                .message(message);
        if (httpStatus == HttpStatus.BAD_REQUEST) {
            throw exceptionBuilder.error(ErrorMessageType.GET_STOCK_INFO_BAD_REQUEST).build().toInfrastructure4xxException();
        }
        throw exceptionBuilder.error(ErrorMessageType.GET_STOCK_INFO_BAD_REQUEST_GENERIC).build();
    }
}
