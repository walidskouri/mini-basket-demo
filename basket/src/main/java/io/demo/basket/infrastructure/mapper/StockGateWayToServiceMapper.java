package io.demo.basket.infrastructure.mapper;

import io.demo.basket.domain.service.stock.ProductStockInfo;
import io.demo.basket.infrastructure.gateway.stock.payload.GatewayProductStockInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StockGateWayToServiceMapper {

    public abstract ProductStockInfo toStock(GatewayProductStockInfo stockInfo);


}
