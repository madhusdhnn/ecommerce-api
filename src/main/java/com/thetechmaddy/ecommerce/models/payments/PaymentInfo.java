package com.thetechmaddy.ecommerce.models.payments;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CASH_ON_DELIVERY;
import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.NOT_NULL_MESSAGE_SUFFIX;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {

    @NotNull(message = "amount" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private BigDecimal amount;

    @NotNull(message = "paymentMode" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private PaymentMode paymentMode;

    public boolean isCashOnDelivery() {
        return CASH_ON_DELIVERY == paymentMode;
    }

}
