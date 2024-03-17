package com.thetechmaddy.ecommerce.exceptions;

public class ProductOutOfStockException extends BusinessException {

    public ProductOutOfStockException(long productId) {
        super(String.format("Product: (productId - %d) is out of stock", productId));
    }

}
