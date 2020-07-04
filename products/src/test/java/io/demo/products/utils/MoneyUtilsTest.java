package io.demo.products.utils;

import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyUtilsTest {

    private static Stream<Arguments> provideAmountToTest() {
        return Stream.of(
                Arguments.of("1500,12€", BigMoney.ofScale(CurrencyUnit.EUR, 150012l, 2)),
                Arguments.of("0,12€", BigMoney.ofScale(CurrencyUnit.EUR, 12l, 2)),
                Arguments.of("5,12€", BigMoney.ofScale(CurrencyUnit.EUR, 512l, 2)),
                Arguments.of("5€", BigMoney.ofScale(CurrencyUnit.EUR, 5l, 0)),
                Arguments.of("5,0€", BigMoney.ofScale(CurrencyUnit.EUR, 50l, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmountToTest")
    public void should_convert_from_string(String input, BigMoney expected) {
        assertThat(MoneyUtils.convertToMoney(input))
                .isEqualTo(expected);
    }


}