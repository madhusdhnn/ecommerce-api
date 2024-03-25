package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.OrderSummary;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.filters.OrderFilters;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.models.responses.Paged;

public interface OrdersService {

    Order getUserOrderInPendingStatus(String userId);

    Order createNewOrder(String userId, OrderRequest orderRequest);

    Order placeOrder(long orderId, CognitoUser customer);

    Order getOrder(long orderId, String userId);

    Paged<OrderSummary> getUserOrders(Integer page, Integer size, String userId, OrderFilters orderFilters);

    void updatePaymentInfo(long orderId, String userId, PaymentInfo paymentInfo);

    void updateDeliveryInfo(long orderId, String userId, DeliveryInfo deliveryInfo);

    void deleteDraftOrder(long orderId, String userId);
}
