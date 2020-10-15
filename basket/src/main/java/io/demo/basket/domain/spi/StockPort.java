package io.demo.basket.domain.spi;


import io.demo.basket.domain.service.stock.ProductStockInfo;

public interface StockPort {

    ProductStockInfo getProductAvailabilityInfo(String productCode);

}
