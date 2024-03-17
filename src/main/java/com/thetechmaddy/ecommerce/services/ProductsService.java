package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.models.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.Paged;

public interface ProductsService {

    Product getProductById(long productId);

    Paged<Product> getAllProducts(Integer page, Integer size, String search, ProductFilters productFilters);

    void ensureProductInStock(long productId);
}