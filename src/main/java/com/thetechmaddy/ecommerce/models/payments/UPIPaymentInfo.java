package com.thetechmaddy.ecommerce.models.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UPIPaymentInfo extends PaymentInfo {

    private String upiId;

}
