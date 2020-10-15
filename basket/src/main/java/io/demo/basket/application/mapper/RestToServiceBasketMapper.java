package io.demo.basket.application.mapper;

import io.demo.basket.application.rest.request.AddProductOfferRequest;
import io.demo.basket.domain.model.basket.offer.Offer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class RestToServiceBasketMapper {


    public List<Offer> toOffers(List<AddProductOfferRequest> offers) {
        if (offers == null) {
            return Collections.emptyList();
        }
        return offers.stream()
                .distinct()
                .map(this::restOfferBodyRequestToOffer)
                .collect(Collectors.toList());
    }

    protected Offer restOfferBodyRequestToOffer(AddProductOfferRequest restOfferBodyRequest) {
        if (restOfferBodyRequest == null) {
            return null;
        }

        Offer serviceOffer = new Offer();

        if (restOfferBodyRequest.getQuantity() != null) {
            serviceOffer.setQuantity(restOfferBodyRequest.getQuantity());
        }

        serviceOffer.setId(restOfferBodyRequest.getProductCode());

        return serviceOffer;
    }


}
