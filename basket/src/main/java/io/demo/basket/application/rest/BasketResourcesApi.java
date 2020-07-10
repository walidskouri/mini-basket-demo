package io.demo.basket.application.rest;

import io.demo.basket.application.rest.exception.RestErrorResponse;
import io.demo.basket.application.rest.request.AddProductRequest;
import io.demo.basket.application.rest.response.RestBasketResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import static io.demo.basket.application.rest.BasketResourcesConstants.USER_LOGIN_LABEL;

@Api
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Response containing the basket of the currently connected customer",
                response = RestBasketResponse.class),
        @ApiResponse(code = 400, message = "Bad Request, the server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing)",
                response = RestErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error",
                response = RestErrorResponse.class),
        @ApiResponse(code = 503, message = "Service Unavailable",
                response = RestErrorResponse.class)
})
public interface BasketResourcesApi {

    @ApiOperation(value = "Get the basket state",
            tags = "Baskets",
            notes = "# Get All the content of the Basket")
    @ApiImplicitParam(name = USER_LOGIN_LABEL, value = "Connected User login", paramType = "header", required = false)
    ResponseEntity<RestBasketResponse> getBasket(@ApiParam(hidden = true) String customerLogin);

    @ApiOperation(value = "Add products",
            tags = "Products",
            notes = "# Add one or many product to users basket")
    @ApiImplicitParam(name = USER_LOGIN_LABEL, value = "Connected User login", paramType = "header", required = false)
    ResponseEntity<RestBasketResponse> addProductsInBasket(
            AddProductRequest addProductRequest,
            @ApiParam(hidden = true) String customerLogin);
}
