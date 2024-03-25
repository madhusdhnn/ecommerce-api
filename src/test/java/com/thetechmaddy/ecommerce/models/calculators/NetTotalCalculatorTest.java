package com.thetechmaddy.ecommerce.models.calculators;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.products.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetTotalCalculatorTest {

    private final TotalCalculator netTotalCalculator = new NetTotalCalculator();

    @Test
    public void testEmptyListReturnZero() {
        BigDecimal actual = netTotalCalculator.calculate(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testNullListReturnZero() {
        BigDecimal actual = netTotalCalculator.calculate(null);
        assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testTotalCalculation() {
        Product product1 = new Product();
        product1.setUnitPrice(new BigDecimal("456.34"));

        Product product2 = new Product();
        product2.setUnitPrice(new BigDecimal("1200.33"));

        BigDecimal actual = netTotalCalculator.calculate(List.of(
                        new CartItem(3, product1),
                        new CartItem(2, product2)
                )
        );

        assertEquals(new BigDecimal("3769.68"), actual);
    }
}
