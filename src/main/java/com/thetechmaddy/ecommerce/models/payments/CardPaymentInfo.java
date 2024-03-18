package com.thetechmaddy.ecommerce.models.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardPaymentInfo extends PaymentInfo {

    private int cvv;
    private String nameOnCard;
    private String cardNumber;
    private String expiryDate;
}
