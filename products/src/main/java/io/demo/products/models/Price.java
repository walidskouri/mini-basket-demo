package io.demo.products.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@Builder
public class Price {

    private Integer amount;
    private String currency;
    private Integer scale;

}
