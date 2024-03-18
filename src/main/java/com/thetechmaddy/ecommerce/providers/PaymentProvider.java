package com.thetechmaddy.ecommerce.providers;

import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;

import java.util.UUID;

public interface PaymentProvider {

    String TRANSACTION_ID = UUID.randomUUID().toString();

    PaymentGatewayResponse processPayment(PaymentGatewayRequest paymentGatewayRequest);

}
