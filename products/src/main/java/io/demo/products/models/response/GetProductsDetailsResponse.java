package io.demo.products.models.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.demo.products.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsDetailsResponse {

    @JsonProperty("content")
    List<Product> content = new ArrayList<>();

}
