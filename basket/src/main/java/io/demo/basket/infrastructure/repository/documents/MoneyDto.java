package io.demo.basket.infrastructure.repository.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MoneyDto {

    private Integer unscaledAmount = null;
    private Integer scale = null;
    private String currency;

}
