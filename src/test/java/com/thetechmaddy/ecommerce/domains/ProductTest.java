package com.thetechmaddy.ecommerce.domains;

import com.thetechmaddy.ecommerce.domains.products.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProductTest {

    @Test
    public void testProductReduceQuantity() {
        Product product = new Product("p1", "pdesc1", new BigDecimal("100.34"), 10);
        product.decrementStockQuantity(5);
        Assertions.assertEquals(5, product.getStockQuantity());

        product.decrementStockQuantity(6);
        Assertions.assertEquals(0, product.getStockQuantity());
    }

    @Test
    public void testIsInStock() {
        Product product = new Product("p1", "pdesc1", new BigDecimal("100.34"), 10);
        Assertions.assertTrue(product.isInStock());

        product.decrementStockQuantity(10);
        Assertions.assertFalse(product.isInStock());
    }
}
