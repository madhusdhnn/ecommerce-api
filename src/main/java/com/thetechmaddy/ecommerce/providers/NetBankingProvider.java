package com.thetechmaddy.ecommerce.providers;

import com.thetechmaddy.ecommerce.exceptions.PaymentInfoRequiredException;
import com.thetechmaddy.ecommerce.exceptions.UnsupportedPaymentInfoTypeException;
import com.thetechmaddy.ecommerce.models.payments.NetBankingPaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Log4j2
public class NetBankingProvider implements PaymentProvider {

    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest paymentGatewayRequest) {
        PaymentInfo paymentInfo = paymentGatewayRequest.getPaymentInfo();

        if (paymentInfo == null) {
            throw new PaymentInfoRequiredException("paymentInfo == null");
        }

        if (paymentInfo instanceof NetBankingPaymentInfo netBankingPaymentInfo) {
            BigDecimal amount = netBankingPaymentInfo.getAmount();
            String senderAccount = paymentGatewayRequest.getSender();
            String receiverAccount = paymentGatewayRequest.getReceiver();
            String currency = paymentGatewayRequest.getCurrency();

            log.info(String.format("Sending money %s %s from %s to %s using bank details - %s",
                    currency, amount, senderAccount, receiverAccount, netBankingPaymentInfo));
            return new PaymentGatewayResponse(true, TRANSACTION_ID);
        }
        throw new UnsupportedPaymentInfoTypeException("NetBankingInfo type required");
    }
}
