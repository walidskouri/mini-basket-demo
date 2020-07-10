package io.demo.basket.application.rest;


import io.demo.basket.application.mapper.ServiceToRestBasketMapper;
import io.demo.basket.application.rest.request.AddProductRequest;
import io.demo.basket.application.rest.response.RestBasketResponse;
import io.demo.basket.application.security.CurrentConnectedUser;
import io.demo.basket.domain.api.BasketServicePort;
import io.demo.basket.domain.model.basket.Basket;
import io.demo.basket.infrastructure.util.logging.CallType;
import io.demo.basket.infrastructure.util.logging.TraceMethodCall;
import io.demo.basket.infrastructure.util.logging.TracingConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.demo.basket.application.rest.BasketResourcesConstants.API_VERSION;
import static io.demo.basket.application.rest.BasketResourcesConstants.BASE_BASKET_URL;

@RestController
@RequestMapping(value = API_VERSION, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class BasketsResources implements BasketResourcesApi {

    private final BasketServicePort basketServicePort;
    private final ServiceToRestBasketMapper serviceToRestBasketMapper;

    private static final String ADD_PRODUCTS_URL = BASE_BASKET_URL + "/products";

    @Override
    @TraceMethodCall(params = "{" + TracingConstant.USER_LOGIN + ": #customerLogin}", type = CallType.RESOURCE)
    @GetMapping(value = BASE_BASKET_URL)
    public ResponseEntity<RestBasketResponse> getBasket(@CurrentConnectedUser String customerLogin) {
        Basket domainBasket = basketServicePort.getBasket(customerLogin);
        RestBasketResponse responseBody = serviceToRestBasketMapper.toTeRestBasketResponse(domainBasket);
        return ResponseEntity.ok().body(responseBody);
    }

    @Override
    @PutMapping(value = ADD_PRODUCTS_URL)
    public ResponseEntity<RestBasketResponse> addProductsInBasket(
            @Validated @RequestBody AddProductRequest addProductRequest,
            @CurrentConnectedUser String customerLogin) {
        Basket domainBasket = basketServicePort.addProducts(customerLogin, addProductRequest.getProductCodes());
        RestBasketResponse responseBody = serviceToRestBasketMapper.toTeRestBasketResponse(domainBasket);
        return ResponseEntity.ok().body(responseBody);
    }


}
