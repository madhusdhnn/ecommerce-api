package com.thetechmaddy.ecommerce.exceptions;

import com.thetechmaddy.ecommerce.models.payments.PaymentMode;

public class PaymentProviderNotImplementedException extends BusinessException {

    public PaymentProviderNotImplementedException(PaymentMode paymentMode) {
        super(String.format("PaymentProvider not implemented for PaymentMode: %s", paymentMode));
    }

}
