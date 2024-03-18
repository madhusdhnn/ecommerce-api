package com.thetechmaddy.ecommerce.exceptions;

import java.util.HashMap;
import java.util.Map;

public class InsufficientProductQuantityException extends BusinessException {

    private final int availableQuantity;
    private final int requestedQuantity;

    public InsufficientProductQuantityException(long productId, int availableQuantity, int requestedQuantity) {
        super(String.format("Product: (productId - %d) has insufficient quantity", productId));
        this.availableQuantity = availableQuantity;
        this.requestedQuantity = requestedQuantity;
    }

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("availableQuantity", availableQuantity);
        attributes.put("requestedQuantity", requestedQuantity);
        return attributes;
    }
}
