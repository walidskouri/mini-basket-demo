package io.demo.basket.application.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "Money")
@Data
public class RestMoney {

    @ApiModelProperty(example = "1500", value = "the unscaled amount")
    @JsonProperty("unscaled_amount")
    private Integer unscaledAmount = null;

    @ApiModelProperty(example = "2", value = "the scale to apply on the amount")
    @JsonProperty("scale")
    private Integer scale = null;

    @ApiModelProperty(example = "EUR", value = "the currency of the amount")
    @JsonProperty("currency")
    private String currency;
}
