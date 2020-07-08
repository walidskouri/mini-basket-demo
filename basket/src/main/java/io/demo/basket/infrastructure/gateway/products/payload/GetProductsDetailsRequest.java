package io.demo.basket.infrastructure.gateway.products.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsDetailsRequest {

    @JsonProperty("product_codes")
    List<String> productCodes;

}