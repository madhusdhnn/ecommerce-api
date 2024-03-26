package com.thetechmaddy.ecommerce.models.calculators;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class TaxAmountTotalCalculator implements TotalCalculator {

    @Override
    public BigDecimal calculate(List<CartItem> cartItems) {
        BigDecimal zero = BigDecimal.ZERO;

        if (cartItems == null) {
            return zero;
        }

        return cartItems.stream()
                .map(cartItem -> cartItem.getProduct()
                        .getTaxAmount()
                        .multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
