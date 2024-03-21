package com.thetechmaddy.ecommerce.models.calculators;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface TotalCalculator {

    BigDecimal calculate(List<CartItem> cartItems);

}
