package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotFoundException;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotInSuccessException;
import com.thetechmaddy.ecommerce.exceptions.PaymentRequiredException;
import com.thetechmaddy.ecommerce.exceptions.UnProcessableEntityException;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CREDIT_CARD;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.DEBIT_CARD;
import static com.thetechmaddy.ecommerce.models.payments.PaymentStatus.*;
import static com.thetechmaddy.ecommerce.utils.PaymentUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentUtilsTest {

    @Test
    public void testInputNull() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> validatePaymentDetailsInputAndPayment(null, new PaymentInfo()));
        assertEquals("payment == null", ex.getMessage());

        ex = assertThrows(NullPointerException.class,
                () -> validatePaymentDetailsInputAndPayment(new Payment(), null));
        assertEquals("paymentInfo == null", ex.getMessage());
    }

    @Test
    public void testValidatePaymentMode() {
        Payment payment = new Payment(PENDING, CREDIT_CARD);
        PaymentInfo paymentInfo = new PaymentInfo(payment.getAmount(), DEBIT_CARD);

        UnProcessableEntityException ex = assertThrows(UnProcessableEntityException.class,
                () -> validatePaymentDetailsInputAndPayment(payment, paymentInfo));

        assertEquals(String.format("Expected paymentMode: (%s), actual paymentMode: (%s)",
                payment.getPaymentMode(), paymentInfo.getPaymentMode()), ex.getMessage());
    }

    @Test
    public void testValidateAmount() {
        Payment payment = new Payment(PENDING, CREDIT_CARD);
        PaymentInfo paymentInfo = new PaymentInfo(new BigDecimal("234.45"), CREDIT_CARD);

        UnProcessableEntityException ex = assertThrows(UnProcessableEntityException.class,
                () -> validatePaymentDetailsInputAndPayment(payment, paymentInfo));

        assertEquals(String.format("Expected amount: (%s), actual amount: (%s)",
                payment.getAmount(), paymentInfo.getAmount()), ex.getMessage());
    }

    @Test
    public void testNotThrows() {
        Payment payment = new Payment(PENDING, CREDIT_CARD);
        PaymentInfo paymentInfo = new PaymentInfo(payment.getAmount(), CREDIT_CARD);
        assertDoesNotThrow(() -> validatePaymentDetailsInputAndPayment(payment, paymentInfo));
    }

    @Test
    public void testPaymentEditableStateTest() {
        Payment payment1 = new Payment(1, SUCCESS);
        UnProcessableEntityException ex = assertThrows(UnProcessableEntityException.class, () -> ensurePaymentInEditableState(payment1));
        assertEquals(String.format("Payment: (paymentId - %d) already completed", payment1.getId()), ex.getMessage());

        Payment payment2 = new Payment(1, PROCESSING);
        ex = assertThrows(UnProcessableEntityException.class, () -> ensurePaymentInEditableState(payment2));
        assertEquals(String.format("Payment: (paymentId - %d) currently processing", payment2.getId()), ex.getMessage());

        assertDoesNotThrow(() -> ensurePaymentInEditableState(new Payment(1, FAILED)));
    }

    @Test
    public void testPaymentStatusSuccess() {
        PaymentNotFoundException ex1 = assertThrows(PaymentNotFoundException.class, () -> ensurePaymentIsSuccess(null));
        assertEquals("Payment details not found", ex1.getMessage());

        Payment payment1 = new Payment(1, PENDING);
        String message = String.format("Payment: (paymentId - %d) not completed. Required amount: %s", payment1.getId(), payment1.getAmount());
        PaymentRequiredException ex2 = assertThrows(PaymentRequiredException.class, () -> ensurePaymentIsSuccess(payment1));
        assertEquals(message, ex2.getMessage());

        List<PaymentStatus> statusList = List.of(FAILED, PROCESSING);
        for (PaymentStatus status : statusList) {
            Payment payment2 = new Payment(1, status);
            message = String.format("Payment: (paymentId - %d) not in success status", payment2.getId());
            PaymentNotInSuccessException ex3 = assertThrows(PaymentNotInSuccessException.class, () -> ensurePaymentIsSuccess(payment2));
            assertEquals(message, ex3.getMessage());
        }

        assertDoesNotThrow(() -> ensurePaymentIsSuccess(new Payment(1, SUCCESS)));
    }
}
