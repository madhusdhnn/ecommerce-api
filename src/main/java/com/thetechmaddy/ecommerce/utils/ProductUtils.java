package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.InsufficientProductQuantityException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductUtils {

    public static void ensureProductHasSufficientQuantity(Product product, int requiredQuantity) {
        if (product.hasInsufficientQuantity(requiredQuantity)) {
            throw new InsufficientProductQuantityException(product.getId(), product.getStockQuantity(), requiredQuantity);
        }
    }
}
