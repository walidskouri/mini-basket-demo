package io.demo.products;

import io.demo.products.models.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPojoTest {

    @Test
    public void create() throws Exception {
        Product pr = new Product("1", "Danao Orange");
        assertThat(pr.getName()).isEqualTo("Danao Orange");

    }

}
