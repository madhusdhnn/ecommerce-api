package com.thetechmaddy.ecommerce.providers;

import com.thetechmaddy.ecommerce.exceptions.PaymentInfoRequiredException;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.Objects;

@Log4j2
public class UPIPaymentProvider implements PaymentProvider {

    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest paymentGatewayRequest) {
        Objects.requireNonNull(paymentGatewayRequest);
        PaymentInfo paymentInfo = paymentGatewayRequest.getPaymentInfo();

        if (paymentInfo == null) {
            throw new PaymentInfoRequiredException("paymentInfo == null");
        }

        BigDecimal amount = paymentInfo.getAmount();
        String senderAccount = paymentGatewayRequest.getSender();
        String receiverAccount = paymentGatewayRequest.getReceiver();
        String currency = paymentGatewayRequest.getCurrency();

        log.info(String.format("Sending money %s %s from %s to %s using UPI ID - %s",
                currency, amount, senderAccount, receiverAccount, paymentInfo));
        return new PaymentGatewayResponse(false, TRANSACTION_ID);
    }
}
