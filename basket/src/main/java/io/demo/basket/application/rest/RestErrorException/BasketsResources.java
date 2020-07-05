package io.demo.basket.application.rest.RestErrorException;


import io.demo.basket.application.rest.BasketResourcesApi;
import io.demo.basket.application.rest.response.RestBasketResponse;
import io.demo.basket.application.rest.response.RestMoney;
import io.demo.basket.application.rest.response.RestOffer;
import io.demo.basket.application.security.CurrentConnectedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static io.demo.basket.application.rest.BasketResourcesConstants.API_VERSION;
import static io.demo.basket.application.rest.BasketResourcesConstants.BASE_BASKET_URL;

@RestController
@RequestMapping(value = API_VERSION, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class BasketsResources implements BasketResourcesApi {

    @Override
    @GetMapping(value = BASE_BASKET_URL)
    public ResponseEntity<RestBasketResponse> getBasket(@CurrentConnectedUser String customerLogin) {
        RestBasketResponse responseBody = new RestBasketResponse();
        responseBody.setCreationDate(OffsetDateTime.now());
        responseBody.setLastModified(OffsetDateTime.now());
        responseBody.setOffers(buildDummyOffers());
        responseBody.setTotalAmount(computeTotalAmount(responseBody.getOffers()));
        return ResponseEntity.ok().body(responseBody);
    }

    private RestMoney computeTotalAmount(List<RestOffer> offers) {
        Integer totalAmount = offers.stream().map(offer -> offer.getQuantity() * offer.getUnitPrice().getUnscaledAmount())
                .reduce(0, Integer::sum);
        RestMoney restMoney = new RestMoney();
        restMoney.setUnscaledAmount(totalAmount);
        restMoney.setScale(2);
        restMoney.setCurrency("EUR");
        return restMoney;
    }

    private List<RestOffer> buildDummyOffers() {
        RestOffer offer1 = new RestOffer();
        offer1.setName("Dummy 1");
        offer1.setProductCode(UUID.randomUUID().toString());
        offer1.setQuantity(1);
        offer1.setUnitPrice(buildRandomOfferPrice());
        RestOffer offer2 = new RestOffer();
        offer2.setName("Dummy 2");
        offer2.setProductCode(UUID.randomUUID().toString());
        offer2.setQuantity(2);
        offer2.setUnitPrice(buildRandomOfferPrice());
        return List.of(offer1, offer2);
    }

    private RestMoney buildRandomOfferPrice() {
        RestMoney restMoney = new RestMoney();
        restMoney.setCurrency("EUR");
        restMoney.setScale(2);
        restMoney.setUnscaledAmount(new Random().nextInt(15) + 1);
        return restMoney;
    }
}
