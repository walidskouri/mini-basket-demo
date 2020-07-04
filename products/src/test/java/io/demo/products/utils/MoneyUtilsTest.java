package io.demo.products.utils;

import io.demo.products.models.Price;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyUtilsTest {

    private static Stream<Arguments> provideAmountToTest() {
        return Stream.of(
                Arguments.of("1500,12€", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(150012).scale(2).build()),
                Arguments.of("0,12€", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(12).scale(2).build()),
                Arguments.of("5,12€", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(512).scale(2).build()),
                Arguments.of("5€", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(5).scale(0).build()),
                Arguments.of("5,0€", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(50).scale(1).build()),
                Arguments.of("5,0$", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(0).scale(0).build()),
                Arguments.of(null, Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(0).scale(0).build())
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmountToTest")
    public void should_convert_from_string(String input, Price expected) {
        assertThat(MoneyUtils.convertToMoney(input))
                .isEqualTo(expected);
    }


}