package io.demo.basket.application.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ApiModel(value = "OfferBodyRequest")
@Data
public class AddProductOfferRequest {

    @ApiModelProperty(value = "The product code to add",
            required = true,
            example = "f594520a-db5a-411b-b129-14c79e44d3d8")
    @NotEmpty(message = "Product code is missing")
    @JsonProperty("product_code")
    private String productCode;

    @ApiModelProperty(value = "Quantity of the product requested by the customer", required = true, example = "2")
    @Min(value = 1, message = "quantity should be higher or equal to 1")
    @NotNull(message = "quantity is missing")
    @JsonProperty("quantity")
    private Integer quantity;

}
