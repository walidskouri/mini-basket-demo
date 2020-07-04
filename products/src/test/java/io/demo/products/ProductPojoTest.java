package io.demo.products;

import io.demo.products.models.Price;
import io.demo.products.models.Product;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPojoTest {

    @Test
    public void create() throws Exception {
        Product pr = new Product("1", "Danao Orange", "11549789874", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(450).scale(2).build());
        assertThat(pr.getName()).isEqualTo("Danao Orange", Price.builder().currency(CurrencyUnit.EUR.getCode()).amount(150).scale(2).build());
    }

}
