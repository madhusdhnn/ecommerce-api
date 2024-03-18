package com.thetechmaddy.ecommerce.models.payments;

import com.thetechmaddy.ecommerce.exceptions.PaymentModeRequiredException;
import com.thetechmaddy.ecommerce.exceptions.PaymentProviderNotImplementedException;
import com.thetechmaddy.ecommerce.providers.CardPaymentProvider;
import com.thetechmaddy.ecommerce.providers.NetBankingProvider;
import com.thetechmaddy.ecommerce.providers.PaymentProvider;
import com.thetechmaddy.ecommerce.providers.UPIPaymentProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.*;

@Component
public class PaymentProviderFactory {

    private static final Map<PaymentMode, PaymentProvider> PAYMENT_PROVIDERS = getPaymentProviders();

    public PaymentProvider getPaymentProvider(PaymentMode paymentMode) {
        if (paymentMode == null) {
            throw new PaymentModeRequiredException("PaymentProvider can not be identified with the null PaymentMode");
        }
        PaymentProvider paymentProvider;
        if ((paymentProvider = PAYMENT_PROVIDERS.get(paymentMode)) == null) {
            throw new PaymentProviderNotImplementedException(paymentMode);
        }
        return paymentProvider;
    }

    private static Map<PaymentMode, PaymentProvider> getPaymentProviders() {
        Map<PaymentMode, PaymentProvider> providerMap = new EnumMap<>(PaymentMode.class);

        providerMap.put(CREDIT_CARD, new CardPaymentProvider());
        providerMap.put(DEBIT_CARD, new CardPaymentProvider());
        providerMap.put(NET_BANKING, new NetBankingProvider());
        providerMap.put(UPI, new UPIPaymentProvider());

        return providerMap;
    }
}
