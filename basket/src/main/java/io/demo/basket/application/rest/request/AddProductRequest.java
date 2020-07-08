package io.demo.basket.application.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@ApiModel(value = "AddProductRequest", description = "Add products to basket")
@Data
public class AddProductRequest {

    @ApiModelProperty(value = "List of products to add",
            required = true,
            example = "[\"f594520a-db5a-411b-b129-14c79e44d3d8\",\"c950540d-dba0-433a-8fa9-d3945e117bf2\",\"01231323-n0yy-1a0z\",\"01235523-n0yy-1a0z\"]")
    @Valid
    @NotEmpty(message = "offers cannot be empty")
    @JsonProperty("product_codes")
    private List<String> productCodes;

}
