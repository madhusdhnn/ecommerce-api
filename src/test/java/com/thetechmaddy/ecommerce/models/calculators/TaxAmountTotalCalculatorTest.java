package com.thetechmaddy.ecommerce.models.calculators;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.products.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxAmountTotalCalculatorTest {

    private final TotalCalculator taxAmountTotalCalculator = new TaxAmountTotalCalculator();

    @Test
    public void testEmptyListReturnZero() {
        BigDecimal actual = taxAmountTotalCalculator.calculate(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testNullListReturnZero() {
        BigDecimal actual = taxAmountTotalCalculator.calculate(null);
        assertEquals(BigDecimal.ZERO, actual);
    }

    @Test
    public void testTotalCalculation() {
        Product product1 = new Product();
        product1.setTaxAmount(new BigDecimal("156.34"));

        Product product2 = new Product();
        product2.setTaxAmount(new BigDecimal("100.33"));

        BigDecimal actual = taxAmountTotalCalculator.calculate(List.of(
                        new CartItem(3, product1),
                        new CartItem(2, product2)
                )
        );

        assertEquals(new BigDecimal("669.68"), actual);
    }
}
