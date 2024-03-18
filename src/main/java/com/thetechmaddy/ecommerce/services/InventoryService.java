package com.thetechmaddy.ecommerce.services;

import java.util.Map;

public interface InventoryService {

    void updateProductsStock(Map<Long, Integer> cartItems);
}
