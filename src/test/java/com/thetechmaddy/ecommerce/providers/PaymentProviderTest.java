package com.thetechmaddy.ecommerce.providers;

import com.thetechmaddy.ecommerce.exceptions.PaymentInfoRequiredException;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentProviderTest {

    @Test
    public void testPaymentInfoRequired() {
        PaymentProvider provider = new NetBankingProvider();
        assertThrows(NullPointerException.class, () -> provider.processPayment(null));
        assertThrows(PaymentInfoRequiredException.class, () -> provider.processPayment(new PaymentGatewayRequest()));
    }

    @Test
    public void testPaymentProvider() {
        PaymentProvider provider = new NetBankingProvider();
        PaymentInfo paymentInfo = new PaymentInfo(new BigDecimal("100"), NET_BANKING);

        PaymentGatewayRequest gatewayRequest = new PaymentGatewayRequest(
                1, "John", "Jane", "INR", paymentInfo);
        PaymentGatewayResponse paymentGatewayResponse = provider.processPayment(gatewayRequest);
        assertNotNull(paymentGatewayResponse);
        assertTrue(paymentGatewayResponse.isSuccess());
    }

    @Test
    public void testPaymentProvider2() {
        PaymentProvider provider = new UPIPaymentProvider();
        PaymentInfo paymentInfo = new PaymentInfo(new BigDecimal("100"), UPI);

        PaymentGatewayRequest gatewayRequest = new PaymentGatewayRequest(
                1, "John", "Jane", "INR", paymentInfo);
        PaymentGatewayResponse paymentGatewayResponse = provider.processPayment(gatewayRequest);
        assertNotNull(paymentGatewayResponse);
        assertFalse(paymentGatewayResponse.isSuccess());
    }

    @Test
    public void testPaymentProvider3() {
        PaymentProvider provider = new CardPaymentProvider();
        PaymentInfo paymentInfo = new PaymentInfo(new BigDecimal("100"), CREDIT_CARD);

        PaymentGatewayRequest gatewayRequest = new PaymentGatewayRequest(
                1, "John", "Jane", "INR", paymentInfo);
        PaymentGatewayResponse paymentGatewayResponse = provider.processPayment(gatewayRequest);
        assertNotNull(paymentGatewayResponse);
        assertTrue(paymentGatewayResponse.isSuccess());
    }
}
