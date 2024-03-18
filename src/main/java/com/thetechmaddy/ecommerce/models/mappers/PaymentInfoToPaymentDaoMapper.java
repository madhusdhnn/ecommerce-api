package com.thetechmaddy.ecommerce.models.mappers;

import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(componentModel = SPRING)
public interface PaymentInfoToPaymentDaoMapper {

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "paymentMode", target = "paymentMode")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "paymentDateTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    Payment mapPaymentInfoToPayment(PaymentInfo paymentInfo);
}
