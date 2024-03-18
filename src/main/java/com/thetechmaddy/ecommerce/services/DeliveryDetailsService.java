package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.DeliveryInfo;

public interface DeliveryDetailsService {

    DeliveryDetails saveDeliveryInfo(DeliveryInfo deliveryInfo, Order order);
}
