package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.repositories.ProductsRepository;
import com.thetechmaddy.ecommerce.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Primary
@Service("inventoryServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class InventoryServiceImpl implements InventoryService {

    private final ProductsRepository productsRepository;

    @Override
    public void updateProductsStock(Map<Long, Integer> productIdQuantityMap) {
        List<Product> products = productsRepository.findAllById(productIdQuantityMap.keySet());

        for (Product product : products) {
            product.decrementStockQuantity(productIdQuantityMap.get(product.getId()));
        }

        productsRepository.saveAll(products);
    }

}
