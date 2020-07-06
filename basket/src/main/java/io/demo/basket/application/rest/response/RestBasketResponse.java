package io.demo.basket.application.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@ApiModel(value = "BasketResponse")
@Data
public class RestBasketResponse {

    @ApiModelProperty(value = "Total offers count")
    @JsonProperty("total_offers_count")
    private Integer totalOffersCount;

    @ApiModelProperty(value = "Offer price")
    @JsonProperty("total_amount")
    private RestMoney totalOffersMonetaryAmount;

    @ApiModelProperty(value = "The list of offers")
    @JsonProperty("offers")
    private List<RestOffer> offers;

    @ApiModelProperty(value = "The basket creation date", example = "2020-07-05T18:00:00Z")
    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;

    @ApiModelProperty(value = "The basket last modification date", example = "2020-07-05T18:00:00Z")
    @JsonProperty("last_modified")
    private OffsetDateTime lastModified;







}
