package io.demo.basket.domain.service;


import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtil {

    private MoneyUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final CurrencyUnit CURRENCY_EURO = CurrencyUnit.EUR;
    private static final int SCALE_EURO = 2;

    public static BigMoney unscaledToMoney(BigDecimal unscaledAmount) {
        return BigMoney.ofScale(CURRENCY_EURO, unscaledAmount, SCALE_EURO, RoundingMode.HALF_UP);
    }

    public static BigMoney unscaledToMoney(long unscaledAmount) {
        return BigMoney.ofScale(CURRENCY_EURO, unscaledAmount, SCALE_EURO);
    }

    public static BigMoney unscaledToMoney(long unscaledAmount, int scale) {
        return BigMoney.ofScale(CURRENCY_EURO, unscaledAmount, scale).withScale(SCALE_EURO, RoundingMode.HALF_UP);
    }

}
