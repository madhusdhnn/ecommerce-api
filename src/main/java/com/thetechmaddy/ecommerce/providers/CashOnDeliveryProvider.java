package com.thetechmaddy.ecommerce.providers;

import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;

public class CashOnDeliveryProvider implements PaymentProvider {
    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest paymentGatewayRequest) {
        return new PaymentGatewayResponse(true, TRANSACTION_ID);
    }
}
