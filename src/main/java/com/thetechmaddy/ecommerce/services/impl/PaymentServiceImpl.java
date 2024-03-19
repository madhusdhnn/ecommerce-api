package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.domains.payments.PaymentModeMaster;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotFoundException;
import com.thetechmaddy.ecommerce.models.mappers.PaymentInfoToPaymentDaoMapper;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import com.thetechmaddy.ecommerce.models.payments.PaymentProviderFactory;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayRequest;
import com.thetechmaddy.ecommerce.models.payments.gateway.PaymentGatewayResponse;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.providers.PaymentProvider;
import com.thetechmaddy.ecommerce.repositories.PaymentModeMasterRepository;
import com.thetechmaddy.ecommerce.repositories.PaymentsRepository;
import com.thetechmaddy.ecommerce.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.thetechmaddy.ecommerce.models.payments.PaymentStatus.*;

@Log4j2
@Primary
@Service("paymentServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PaymentServiceImpl implements PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final PaymentProviderFactory paymentProviderFactory;
    private final PaymentModeMasterRepository paymentModeMasterRepository;
    private final PaymentInfoToPaymentDaoMapper paymentInfoToPaymentDaoMapper;

    @Override
    public List<PaymentMode> getSupportedPaymentModes() {
        return paymentModeMasterRepository.findAllBySupportedTrue()
                .stream().map(PaymentModeMaster::getPaymentMode)
                .collect(Collectors.toList());
    }

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo, Order order) {
        Payment newPayment = paymentInfoToPaymentDaoMapper.mapPaymentInfoToPayment(paymentInfo);
        newPayment.setStatus(PENDING);
        newPayment.setOrder(order);
        order.setPayment(newPayment);
        this.paymentsRepository.save(newPayment);
    }

    @Override
    @Transactional
    public Payment processPayment(long idempotencyId, PaymentInfo paymentInfo, CognitoUser user) {
        Payment payment = paymentsRepository.findById(idempotencyId)
                .orElseThrow(() -> new PaymentNotFoundException(idempotencyId));

        if (payment.isPending()) {
            payment.setStatus(PROCESSING);
            payment = paymentsRepository.save(payment);

            PaymentProvider paymentProvider = paymentProviderFactory.getPaymentProvider(payment.getPaymentMode());
            PaymentGatewayRequest paymentGatewayRequest = new PaymentGatewayRequest(
                    user.getFullName(), "Jane Doe", "INR", paymentInfo
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
