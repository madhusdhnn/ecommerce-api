package com.thetechmaddy.ecommerce.models.payments.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentGatewayResponse {

    private boolean success;
    private String transactionId;

}
