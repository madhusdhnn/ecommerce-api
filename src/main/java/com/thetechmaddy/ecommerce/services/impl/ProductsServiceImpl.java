package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.domains.products.ReservedProduct;
import com.thetechmaddy.ecommerce.exceptions.InsufficientProductQuantityException;
import com.thetechmaddy.ecommerce.exceptions.ProductNotFoundException;
import com.thetechmaddy.ecommerce.models.filters.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.repositories.ProductsRepository;
import com.thetechmaddy.ecommerce.repositories.ReservedProductsRepository;
import com.thetechmaddy.ecommerce.repositories.specifications.GetProductsSpecification;
import com.thetechmaddy.ecommerce.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Primary
@Service("productsServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final ReservedProductsRepository reservedProductsRepository;

    @Override
    public void reserveProducts(Order order, Map<Long, Integer> productIdQuantityMap) {
        List<Product> products = productsRepository.findAllById(productIdQuantityMap.keySet());

        List<ReservedProduct> reservedProducts = new ArrayList<>();
        for (Product product : products) {
            int quantityToDecrement = productIdQuantityMap.get(product.getId());
            product.decrementStockQuantity(quantityToDecrement);
            reservedProducts.add(new ReservedProduct(quantityToDecrement, order, product));
        }

        productsRepository.saveAll(products);
        reservedProductsRepository.saveAll(reservedProducts);
    }

    @Override
    public Product getProductById(long productId) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id : %d", productId)));
    }

    @Override
    public Product checkQuantityAndGetProduct(long productId, int requiredQuantity) {
        Product product = getProductById(productId);
        if (product.hasInsufficientQuantity(requiredQuantity)) {
            throw new InsufficientProductQuantityException(productId, product.getStockQuantity(), requiredQuantity);
        }
        return product;
    }

    @Override
    public Paged<Product> getAllProducts(Integer page, Integer size, String search, ProductFilters productFilters) {
        Specification<Product> specification = new GetProductsSpecification(search, productFilters);
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Product> products = this.productsRepository.findAll(specification, pageRequest);
        return new Paged<>(products.getContent(), page, products.getTotalElements(), products.getNumberOfElements());
    }

    @Override
    public void ensureProductHasSufficientQuantity(long productId, int requiredQuantity) {
        Product product = getProductById(productId);

        if (product.hasInsufficientQuantity(requiredQuantity)) {
            throw new InsufficientProductQuantityException(productId, product.getStockQuantity(), requiredQuantity);
        }
    }

    @Override
    @Transactional // TODO: This method should be called when order gets returned /cancelled.
    public void restoreProductQuantity(long orderId, List<Long> productIds) {
        List<ReservedProduct> reservedProducts = reservedProductsRepository.findAllByOrderIdAndProductIdIn(orderId, productIds);

        List<Product> products = new ArrayList<>();
        for (ReservedProduct reservedProduct : reservedProducts) {
            Product product = reservedProduct.getProduct();
            product.incrementStockQuantity(reservedProduct.getQuantity());
            products.add(product);
        }
        productsRepository.saveAll(products);
        reservedProductsRepository.deleteByOrderId(orderId);
    }
}
