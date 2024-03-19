package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;

import java.util.List;

public interface PaymentService {

    List<PaymentMode> getSupportedPaymentModes();

    void savePaymentInfo(PaymentInfo paymentInfo, Order order);

    Payment processPayment(long idempotencyId, PaymentInfo paymentInfo, CognitoUser user);

    Payment getStatus(long idempotencyId);
}
