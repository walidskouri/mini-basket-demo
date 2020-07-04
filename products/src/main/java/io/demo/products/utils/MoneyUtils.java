package io.demo.products.utils;


import io.demo.products.models.Price;
import org.joda.money.CurrencyUnit;
import org.springframework.util.StringUtils;

public class MoneyUtils {

    private static final String CURRENCY_STRING = "€";

    public static Price convertToMoney(String priceInString) {
        if (StringUtils.isEmpty(priceInString) || priceInString.indexOf(CURRENCY_STRING) == -1) {
            return Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(0).scale(0).build();
        }
        String currency = CURRENCY_STRING;
        String rawAmount = priceInString.substring(0, priceInString.indexOf(currency));
        int scale = rawAmount.indexOf(",") == -1 ? 0 : rawAmount.length() - rawAmount.indexOf(",") - 1;
        String amountWithoutScale = rawAmount.replace(",", "");
        return Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(Integer.valueOf(amountWithoutScale)).scale(scale).build();
    }

}
