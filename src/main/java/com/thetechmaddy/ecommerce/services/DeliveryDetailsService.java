package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.DeliveryInfo;

public interface DeliveryDetailsService {

    void saveDeliveryInfo(DeliveryInfo deliveryInfo, Order order);
}
