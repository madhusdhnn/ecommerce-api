package com.thetechmaddy.ecommerce.models.mappers;

import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING)
public interface DeliveryInfoToDeliveryDetailsDaoMapper {

    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "shippingAddress.addressOne", target = "shippingAddressOne")
    @Mapping(source = "shippingAddress.addressTwo", target = "shippingAddressTwo")
    @Mapping(source = "shippingAddress.city", target = "shippingCity")
    @Mapping(source = "shippingAddress.state", target = "shippingState")
    @Mapping(source = "shippingAddress.zipCode", target = "shippingZipCode")
    @Mapping(source = "billingAddress.addressOne", target = "billingAddressOne")
    @Mapping(source = "billingAddress.addressTwo", target = "billingAddressTwo")
    @Mapping(source = "billingAddress.city", target = "billingCity")
    @Mapping(source = "billingAddress.state", target = "billingState")
    @Mapping(source = "billingAddress.zipCode", target = "billingZipCode")
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeliveryDetails mapDeliveryInfoToDeliveryDetails(DeliveryInfo deliveryInfo);
}
