package com.thetechmaddy.ecommerce.models.payments.gateway;

import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentGatewayRequest {

    private long idempotencyId;
    private String sender;
    private String receiver;
    private String currency;
    private PaymentInfo paymentInfo;
}
