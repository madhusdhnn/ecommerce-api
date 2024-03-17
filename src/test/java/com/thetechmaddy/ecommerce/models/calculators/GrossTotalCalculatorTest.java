package com.thetechmaddy.ecommerce.models.calculators;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.products.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrossTotalCalculatorTest {

    private final OrderTotalCalculator grossTotalCalculator = new GrossTotalCalculator();

    @Test
    public void testEmptyListReturnZero() {
        BigDecimal actual = grossTotalCalculator.calculate(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testNullListReturnZero() {
        BigDecimal actual = grossTotalCalculator.calculate(null);
        assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testTotalCalculation() {
        Product product1 = new Product();
        product1.setGrossAmount(new BigDecimal("876.34"));

        Product product2 = new Product();
        product2.setGrossAmount(new BigDecimal("1399.00"));

        BigDecimal actual = grossTotalCalculator.calculate(List.of(
                        new CartItem(3, product1),
                        new CartItem(2, product2)
                )
        );

        assertEquals(new BigDecimal("5427.02"), actual);
    }
}
