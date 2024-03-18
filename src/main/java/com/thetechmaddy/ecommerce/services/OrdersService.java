package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;

public interface OrdersService {

    Order initiateOrder(String userId, OrderRequest orderRequest);

    Order createNewOrder(String userId, OrderRequest orderRequest);

    Order placeOrder(long orderId, CognitoUser customer);
}
