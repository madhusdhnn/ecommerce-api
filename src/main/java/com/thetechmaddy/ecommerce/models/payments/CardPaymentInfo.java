package com.thetechmaddy.ecommerce.models.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardPaymentInfo extends PaymentInfo {

    private int cvv;
    private String nameOnCard;
    private String cardNumber;
    private String expiryDate;

    public CardPaymentInfo(BigDecimal amount, PaymentMode paymentMode) {
        super(amount, paymentMode);
    }
}
