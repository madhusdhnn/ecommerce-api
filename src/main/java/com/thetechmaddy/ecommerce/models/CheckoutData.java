package com.thetechmaddy.ecommerce.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CheckoutData {

    private long cartId;
    private BigDecimal cartTotal;

}
