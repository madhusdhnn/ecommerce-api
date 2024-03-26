package com.thetechmaddy.ecommerce.models.calculators;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class NetAmountTotalCalculator implements TotalCalculator {

    @Override
    public BigDecimal calculate(List<CartItem> cartItems) {
        BigDecimal zero = BigDecimal.ZERO;

        if (cartItems == null) {
            return zero;
        }

        return cartItems.stream()
                .map(cartItem -> cartItem.getProduct()
                        .getUnitPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                ).reduce(zero, BigDecimal::add);
    }

}
