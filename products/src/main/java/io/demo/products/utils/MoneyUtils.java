package io.demo.products.utils;

import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

public class MoneyUtils {

    public static BigMoney convertToMoney(String priceInString) {
        String currency = "€";
        String rawAmount = priceInString.substring(0, priceInString.indexOf(currency));
        int scale = rawAmount.indexOf(",") == -1 ? 0 : rawAmount.length() - rawAmount.indexOf(",") - 1;
        System.out.println(scale);
        String amountWithoutScale = rawAmount.replace(",", "");
        BigMoney moneyProce = BigMoney.ofScale(CurrencyUnit.EUR, Long.valueOf(amountWithoutScale), scale);
        return moneyProce;
    }

}
