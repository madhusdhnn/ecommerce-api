package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;

public interface OrdersService {

    Order getPendingOrder(String userId);

    Order createNewOrder(String userId, OrderRequest orderRequest);

    Order placeOrder(long orderId, CognitoUser customer);

    Order getOrder(long orderId, String userId);
}
