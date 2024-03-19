package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.models.responses.Paged;

public interface OrdersService {

    Order getUserOrderInPendingStatus(String userId);

    Order createNewOrder(String userId, OrderRequest orderRequest);

    Order placeOrder(long orderId, CognitoUser customer);

    Order getOrder(long orderId, String userId);

    Paged<Order> getUserOrders(Integer page, Integer size, String userId);
}
