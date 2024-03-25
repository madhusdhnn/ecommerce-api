package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.mappers.DeliveryInfoToDeliveryDetailsDaoMapper;
import com.thetechmaddy.ecommerce.repositories.DeliveryDetailsRepository;
import com.thetechmaddy.ecommerce.services.DeliveryDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("deliveryDetailsServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DeliveryDetailsServiceImpl implements DeliveryDetailsService {

    private final DeliveryDetailsRepository deliveryDetailsRepository;
    private final DeliveryInfoToDeliveryDetailsDaoMapper deliveryInfoToDeliveryDetailsDaoMapper;

    @Override
    public void saveDeliveryInfo(DeliveryInfo deliveryInfo, Order order) {
        if (order == null) {
            throw new NullPointerException("order == null");
        }

        DeliveryDetails deliveryDetails = deliveryInfoToDeliveryDetailsDaoMapper.mapDeliveryInfoToDeliveryDetails(deliveryInfo);
        deliveryDetails.setOrder(order);
        order.setDeliveryDetails(deliveryDetails);
        this.deliveryDetailsRepository.save(deliveryDetails);
    }
}
