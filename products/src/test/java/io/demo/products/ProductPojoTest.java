package io.demo.products;

import io.demo.products.models.Product;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPojoTest {

    @Test
    public void create() throws Exception {
        Product pr = new Product("1", "Danao Orange", "11549789874", BigMoney.ofScale(CurrencyUnit.EUR, 450, 2));
        assertThat(pr.getName()).isEqualTo("Danao Orange", BigMoney.ofScale(CurrencyUnit.EUR, 150, 2));
    }

}
