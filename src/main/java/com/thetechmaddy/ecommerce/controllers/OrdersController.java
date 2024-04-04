package com.thetechmaddy.ecommerce.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.exceptions.UnsupportedBusinessOperationException;
import com.thetechmaddy.ecommerce.models.*;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.filters.OrderFilters;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.services.CartsService;
import com.thetechmaddy.ecommerce.services.OrdersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.thetechmaddy.ecommerce.models.AppConstants.CURRENT_USER_REQUEST_ATTRIBUTE;
import static com.thetechmaddy.ecommerce.models.AppConstants.DEFAULT_DELETE_ORDER_STATUS;
import static com.thetechmaddy.ecommerce.models.OrderStatus.PENDING;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@SecurityRequirement(name = AppConstants.SECURITY_SCHEME_NAME)
public class OrdersController extends BaseController {

    private final OrdersService ordersService;
    private final CartsService cartsService;

    @PostMapping("/initiate")
    @JsonView({JsonViews.OrderInitiateResponse.class})
    public ApiResponse<Order> initiateOrder(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                            @RequestBody @Valid OrderRequest orderRequest) {
        String userId = cognitoUser.getCognitoSub();

        Cart cart = cartsService.getCart(orderRequest.getCartId(), userId);
        List<CartItem> cartItems = cart.getCartItems();

        Order order = ordersService.getUserOrderInPendingStatus(userId);
        if (order == null) {
            return ApiResponse.success(ordersService.createNewOrder(userId, orderRequest));
        }

        CartItemsOrderItemsMatcher itemsMatcher = new CartItemsOrderItemsMatcher(cartItems, order.getOrderItems());
        if (itemsMatcher.matches()) {
            return ApiResponse.success(order);
        }

        ordersService.deletePendingOrder(order.getId(), userId);
        cartsService.lockCart(cart.getId(), userId);
        return ApiResponse.success(ordersService.createNewOrder(userId, orderRequest));

    }

    @PutMapping("/{orderId}/payment")
    public ApiResponse<?> updatePayment(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                        @PathVariable("orderId") long orderId,
                                        @RequestBody @Valid PaymentInfo paymentInfo) {
        ordersService.updatePaymentInfo(orderId, cognitoUser.getCognitoSub(), paymentInfo);
        return ApiResponse.success();
    }

    @PutMapping("/{orderId}/delivery")
    public ApiResponse<?> updateDelivery(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                         @PathVariable("orderId") long orderId,
                                         @RequestBody @Valid DeliveryInfo deliveryInfo) {
        ordersService.updateDeliveryInfo(orderId, cognitoUser.getCognitoSub(), deliveryInfo);
        return ApiResponse.success();
    }

    @PutMapping("/{orderId}/place")
    @JsonView(JsonViews.PlaceOrderResponse.class)
    public ApiResponse<?> placeOrder(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                     @PathVariable("orderId") long orderId) {
        return ApiResponse.success(ordersService.placeOrder(orderId, cognitoUser));
    }

    @GetMapping("/{orderId}")
    @JsonView(JsonViews.GetOrderDetailResponse.class)
    public ApiResponse<Order> getOrder(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                       @PathVariable("orderId") long orderId) {
        return ApiResponse.success(ordersService.getOrder(orderId, cognitoUser.getCognitoSub()));
    }

    @GetMapping
    @JsonView(JsonViews.GetOrderResponse.class)
    public Paged<OrderSummary> getOrders(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                         @RequestParam(name = "page") Integer page,
                                         @RequestParam(name = "size") Integer size,
                                         @RequestParam(name = "year", required = false) Integer year,
                                         @RequestParam(name = "last30Days", required = false) Boolean last30Days,
                                         @RequestParam(name = "past3Months", required = false) Boolean past3Months,
                                         @RequestParam(name = "orderStatus", required = false) OrderStatus orderStatus) {
        OrderFilters orderFilters = OrderFilters.builder()
                .year(year).last30Days(last30Days)
                .past3Months(past3Months).orderStatus(orderStatus)
                .build();
        return ordersService.getUserOrders(page, size, cognitoUser.getCognitoSub(), orderFilters);
    }

    @DeleteMapping("/{orderId}")
    public ApiResponse<?> deleteOrder(@RequestAttribute(name = CURRENT_USER_REQUEST_ATTRIBUTE) CognitoUser cognitoUser,
                                      @PathVariable("orderId") long orderId,
                                      @RequestParam(name = "status", required = false,
                                              defaultValue = DEFAULT_DELETE_ORDER_STATUS) String deleteOrderStatus) {
        if (OrderStatus.parse(deleteOrderStatus) != PENDING) {
            throw new UnsupportedBusinessOperationException("Delete order other than PENDING status not supported");
        }

        ordersService.deletePendingOrder(orderId, cognitoUser.getCognitoSub());
        return ApiResponse.success();
    }
}
