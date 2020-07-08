package io.demo.basket.infrastructure.mapper;

import io.demo.basket.application.rest.response.RestOffer;
import io.demo.basket.domain.model.basket.offer.Offer;
import io.demo.basket.domain.model.basket.offer.Product;
import io.demo.basket.domain.service.MoneyUtil;
import io.demo.basket.infrastructure.gateway.products.payload.GatewayPrice;
import io.demo.basket.infrastructure.gateway.products.payload.GatewayProduct;
import org.joda.money.BigMoney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductGateWayToServiceMapper {

    @Mapping(expression = "java(toRestMoney(product.getUnitPrice()))", target = "unitPrice")
    protected abstract Product toProduct(GatewayProduct product);

    public abstract List<Product> toProducts(List<GatewayProduct> offers);

    public BigMoney toRestMoney(GatewayPrice money) {
        if (money != null) {
            return MoneyUtil.unscaledToMoney(money.getAmount(), money.getScale());
        } else return null;
    }

}
