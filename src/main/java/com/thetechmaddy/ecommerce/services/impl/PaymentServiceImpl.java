package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.exceptions.OrderItemsTotalMismatchException;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotFoundException;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import com.thetechmaddy.ecommerce.models.mappers.PaymentInfoToPaymentDaoMapper;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentProviderFactory;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.providers.PaymentProvider;
import com.thetechmaddy.ecommerce.repositories.OrderItemsRepository;
import com.thetechmaddy.ecommerce.repositories.PaymentsRepository;
import com.thetechmaddy.ecommerce.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.thetechmaddy.ecommerce.models.payments.PaymentStatus.*;

@Log4j2
@Primary
@Service("paymentServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PaymentServiceImpl implements PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final PaymentProviderFactory paymentProviderFactory;
    private final PaymentInfoToPaymentDaoMapper paymentInfoToPaymentDaoMapper;

    @Override
    public Payment savePaymentInfo(PaymentInfo paymentInfo, Order order) {
        if (order == null) {
            throw new NullPointerException("order == null");
        }

        Payment payment = paymentInfoToPaymentDaoMapper.mapPaymentInfoToPayment(paymentInfo)
                .toBuilder()
                .status(PENDING)
                .order(order)
                .build();

        return this.paymentsRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment processPayment(long idempotencyId, PaymentInfo paymentInfo, CognitoUser user) {
        Payment payment = paymentsRepository.findById(idempotencyId)
                .orElseThrow(() -> new PaymentNotFoundException(idempotencyId));

        BigDecimal grossTotal = orderItemsRepository.getTotal(user.getCognitoSub(), OrderStatus.PENDING);
        if (paymentInfo.getAmount().compareTo(grossTotal) != 0) {
            throw new OrderItemsTotalMismatchException(
                    String.format("Order items total and payment process request amount do not match. Order Total: %s. Payment requested: %s",
                            grossTotal, paymentInfo.getAmount())
            );
        }

        if (payment.isPending()) {
            payment.setStatus(PROCESSING);
            payment = paymentsRepository.save(payment);

            PaymentProvider paymentProvider = paymentProviderFactory.getPaymentProvider(paymentInfo.getPaymentMode());
            PaymentGatewayRequest paymentGatewayRequest = new PaymentGatewayRequest(
                    user.getFullName(), "Jane Doe", paymentInfo.getAmount(), "INR"
            );
            PaymentGatewayResponse gatewayResponse = paymentProvider.processPayment(paymentGatewayRequest);

            payment.setStatus(gatewayResponse.isSuccess() ? SUCCESS : FAILED);
            payment.setPaymentDateTime(OffsetDateTime.now());

            log.info(String.format("Payment: (paymentId - %d) completed with status %s and transactionId - %s", payment.getId(), payment.getStatus(), gatewayResponse.getTransactionId()));

            return paymentsRepository.save(payment);
        }

        return payment;
    }

    @Override
    public Payment getStatus(long idempotencyId) {
        return paymentsRepository.findById(idempotencyId)
                .orElseThrow(() -> new PaymentNotFoundException(idempotencyId));
    }
}
