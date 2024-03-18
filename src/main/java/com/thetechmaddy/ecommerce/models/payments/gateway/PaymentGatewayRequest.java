package com.thetechmaddy.ecommerce.models.payments.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentGatewayRequest {

    private String sender;
    private String receiver;
    private BigDecimal amount;
    private String currency;
}
