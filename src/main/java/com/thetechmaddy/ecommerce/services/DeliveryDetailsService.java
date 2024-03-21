package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;

public interface DeliveryDetailsService {

    void saveDeliveryInfo(DeliveryInfo deliveryInfo, Order order);
}
