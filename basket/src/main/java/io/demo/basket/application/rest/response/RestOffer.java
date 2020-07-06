package io.demo.basket.application.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Offer")
@Data
public class RestOffer {

    @ApiModelProperty(example = "1", required = true, value = "This is the quantity of an offer")
    @JsonProperty("quantity")
    private Integer quantity;

    @ApiModelProperty(example = "Spring Roll Veg Mini", value = "Product name")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(example = "3cf84cb2-b985-443f-ab85-228d4ab8ae0b", value = "Unique product id")
    @JsonProperty("product_code")
    private String productCode;

    @ApiModelProperty(value = "Offer unit price")
    @JsonProperty("unit_price")
    private RestMoney unitPrice;

    @ApiModelProperty(value = "Offer total price")
    @JsonProperty("total_price")
    private RestMoney linePrice;



}
