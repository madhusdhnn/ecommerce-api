package com.thetechmaddy.ecommerce.providers;

import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Log4j2
public class UPIPaymentProvider implements PaymentProvider {

    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest paymentGatewayRequest) {
        BigDecimal amount = paymentGatewayRequest.getAmount();
        String senderAccount = paymentGatewayRequest.getSender();
        String receiverAccount = paymentGatewayRequest.getReceiver();
        String currency = paymentGatewayRequest.getCurrency();

        log.info(String.format("Sending money %s %s from %s to %s", currency, amount, senderAccount, receiverAccount));
        return new PaymentGatewayResponse(false, TRANSACTION_ID);
    }

}
