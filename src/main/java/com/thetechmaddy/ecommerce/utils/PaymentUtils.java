package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotFoundException;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotInSuccessException;
import com.thetechmaddy.ecommerce.exceptions.PaymentRequiredException;
import com.thetechmaddy.ecommerce.exceptions.UnProcessableEntityException;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentUtils {

    public static void validatePaymentDetailsInputAndPayment(Payment payment, PaymentInfo paymentInfo) {
        Objects.requireNonNull(payment, "payment == null");
        Objects.requireNonNull(paymentInfo, "paymentInfo == null");

        if (payment.getPaymentMode() != paymentInfo.getPaymentMode()) {
            throw new UnProcessableEntityException(
                    String.format("Expected paymentMode: (%s), actual paymentMode: (%s)",
                            payment.getPaymentMode(), paymentInfo.getPaymentMode()));
        } else if (payment.getAmount().compareTo(paymentInfo.getAmount()) != 0) {
            throw new UnProcessableEntityException(
                    String.format("Expected amount: (%s), actual amount: (%s)",
                            payment.getAmount(), paymentInfo.getAmount()));
        }
    }

    public static void ensurePaymentInEditableState(Payment payment) {
        Objects.requireNonNull(payment, "payment == null");

        if (payment.isSuccess()) {
            throw new UnProcessableEntityException(String.format("Payment: (paymentId - %d) already completed", payment.getId()));
        } else if (payment.isProcessing()) {
            throw new UnProcessableEntityException(String.format("Payment: (paymentId - %d) currently processing", payment.getId()));
        }
    }

    public static void ensurePaymentIsSuccess(Payment payment) {
        if (payment == null) {
            throw new PaymentNotFoundException("Payment details not found");
        }

        if (payment.isPending()) {
            throw new PaymentRequiredException(
                    String.format("Payment: (paymentId - %d) not completed. Required amount: %s", payment.getId(), payment.getAmount()));
        }

        if (!payment.isSuccess()) {
            throw new PaymentNotInSuccessException(
                    String.format("Payment: (paymentId - %d) not in success status", payment.getId()));
        }
    }
}
