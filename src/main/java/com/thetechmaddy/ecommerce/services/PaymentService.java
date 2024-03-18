package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;

import java.util.function.Function;

public interface PaymentService {

    Payment savePaymentInfo(PaymentInfo paymentInfo, Function<Payment, Payment> paymentSaveHandler);

    Payment processPayment(long idempotencyId, PaymentInfo paymentInfo, CognitoUser user);

    Payment getStatus(long idempotencyId);
}
