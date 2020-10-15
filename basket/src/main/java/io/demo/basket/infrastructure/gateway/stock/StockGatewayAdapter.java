package io.demo.basket.infrastructure.gateway.stock;

import io.demo.basket.domain.service.stock.ProductStockInfo;
import io.demo.basket.domain.spi.StockPort;
import io.demo.basket.infrastructure.gateway.stock.payload.GatewayProductStockInfo;
import io.demo.basket.infrastructure.mapper.StockGateWayToServiceMapper;
import io.demo.basket.infrastructure.setting.StockSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static io.demo.basket.domain.exception.ErrorMessageType.GET_STOCK_INFO_BAD_REQUEST_GENERIC;
import static io.demo.basket.infrastructure.gateway.GatewayConstants.STOCK_EX_OBJ;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockGatewayAdapter implements StockPort {

    private final StockSettings stockSettings;
    private final StockGateWayToServiceMapper stockGateWayToServiceMapper;
    private final RestTemplate stockRestTemplate;

    public ProductStockInfo getProductAvailabilityInfo(String productCode) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(stockSettings.getBaseUri())
                .path(productCode)
                .build();
        ResponseEntity<GatewayProductStockInfo> response = null;
        try {
            response = stockRestTemplate.getForEntity(uriComponents.toUri(), GatewayProductStockInfo.class);
        } catch (Exception e) {
            StockErrorHandler.throwInfrastructureException(e, GET_STOCK_INFO_BAD_REQUEST_GENERIC, STOCK_EX_OBJ);
        }
        return stockGateWayToServiceMapper.toStock(response.getBody());
    }

}
