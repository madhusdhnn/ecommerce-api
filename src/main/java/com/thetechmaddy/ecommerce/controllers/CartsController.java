package com.thetechmaddy.ecommerce.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.Cart;
import com.thetechmaddy.ecommerce.models.AppConstants;
import com.thetechmaddy.ecommerce.models.JsonViews.CartResponse;
import com.thetechmaddy.ecommerce.models.requests.CartItemRequest;
import com.thetechmaddy.ecommerce.models.requests.CartItemUpdateRequest;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.services.CartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/carts")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartsController extends BaseController {

    private final CartsService cartsService;

    @GetMapping("/{cartId}")
    @JsonView(CartResponse.class)
    public ApiResponse<Cart> getCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                     @PathVariable("cartId") long cartId) {
        return ApiResponse.success(this.cartsService.getCart(cartId, cognitoUser.getCognitoSub()));
    }

    @PostMapping("/{cartId}/items")
    public ApiResponse<?> addProductToCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                           @PathVariable("cartId") long cartId,
                                           @RequestBody CartItemRequest cartItemRequest) {
        this.cartsService.addProductToCart(cartId, cognitoUser.getCognitoSub(), cartItemRequest);
        return ApiResponse.success();
    }

    @PatchMapping("/{cartId}/items/{productId}")
    public ApiResponse<?> updateProductInCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                              @PathVariable("cartId") long cartId,
                                              @PathVariable("productId") long productId,
                                              @RequestBody CartItemUpdateRequest cartItemUpdateRequest) {
        this.cartsService.updateProductInCart(cartId, productId, cognitoUser.getCognitoSub(), cartItemUpdateRequest);
        return ApiResponse.success();
    }

    @PatchMapping("/{cartId}/lock")
    public ApiResponse<?> lockCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                   @PathVariable("cartId") long cartId) {
        this.cartsService.lockCart(cartId, cognitoUser.getCognitoSub());
        return ApiResponse.success();
    }

    @PatchMapping("/{cartId}/lock/release")
    public ApiResponse<?> unlockCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                     @PathVariable("cartId") long cartId) {
        this.cartsService.unlockCart(cartId, cognitoUser.getCognitoSub());
        return ApiResponse.success();
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ApiResponse<?> removeProductFromCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                                @PathVariable("cartId") long cartId,
                                                @PathVariable("productId") long productId) {
        this.cartsService.removeProductFromCart(cartId, productId, cognitoUser.getCognitoSub());
        return ApiResponse.success();
    }

    @DeleteMapping("/{cartId}/items")
    public ApiResponse<?> clearCart(@RequestAttribute(name = AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                    @PathVariable("cartId") long cartId) {
        this.cartsService.clearCart(cartId, cognitoUser.getCognitoSub());
        return ApiResponse.success();
    }
}