package com.thetechmaddy.ecommerce.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.JsonViews;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.services.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.thetechmaddy.ecommerce.models.AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrdersController extends BaseController {

    private final OrdersService ordersService;

    @PostMapping("/initiate")
    @JsonView({JsonViews.OrderInitiateResponse.class})
    public ApiResponse<Order> initiateOrder(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                            @RequestBody @Valid OrderRequest orderRequest) {

        Order pendingOrder = ordersService.getPendingOrder(cognitoUser.getCognitoSub());

        if (pendingOrder == null) {
            pendingOrder = ordersService.createNewOrder(cognitoUser.getCognitoSub(), orderRequest);
        }

        return ApiResponse.success(pendingOrder);
    }

    @PutMapping("/{orderId}/place")
    @JsonView(JsonViews.PlaceOrderResponse.class)
    public ApiResponse<?> placeOrder(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                     @PathVariable("orderId") long orderId) {
        return ApiResponse.success(ordersService.placeOrder(orderId, cognitoUser));
    }

}
