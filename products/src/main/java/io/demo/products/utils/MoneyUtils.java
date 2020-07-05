package io.demo.products.utils;


import io.demo.products.models.Price;
import org.joda.money.CurrencyUnit;
import org.springframework.util.StringUtils;

public class MoneyUtils {

    public static Price convertToMoney(String priceInString) {
        if (StringUtils.isEmpty(priceInString)) {
            return Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(0).scale(0).build();
        }
        int scale = priceInString.indexOf(".") == -1 ? 0 : priceInString.length() - priceInString.indexOf(".") - 1;
        String amountWithoutScale = priceInString.replace(".", "");
        return Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(Integer.valueOf(amountWithoutScale)).scale(scale).build();
    }

}
