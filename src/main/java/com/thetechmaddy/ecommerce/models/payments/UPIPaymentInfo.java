package com.thetechmaddy.ecommerce.models.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UPIPaymentInfo extends PaymentInfo {

    private String upiId;

    public UPIPaymentInfo(BigDecimal amount, PaymentMode paymentMode) {
        super(amount, paymentMode);
    }
}
