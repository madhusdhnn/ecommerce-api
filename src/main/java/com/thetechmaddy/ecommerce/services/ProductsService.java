package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.models.filters.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.Paged;

import java.util.List;
import java.util.Map;

public interface ProductsService {

    void reserveProducts(Order order, Map<Long, Integer> productIdQuantityMap);

    Product getProductById(long productId);

    Product checkQuantityAndGetProduct(long productId, int requestedQuantity);

    Paged<Product> getAllProducts(Integer page, Integer size, String search, ProductFilters productFilters);

    void ensureProductHasSufficientQuantity(long productId, int requiredQuantity);

    void releaseReservedProducts(long orderId, List<Long> productIds);
}