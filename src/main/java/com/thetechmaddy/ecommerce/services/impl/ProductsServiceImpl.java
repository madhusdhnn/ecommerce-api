package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.InsufficientProductQuantityException;
import com.thetechmaddy.ecommerce.exceptions.ProductNotFoundException;
import com.thetechmaddy.ecommerce.exceptions.ProductOutOfStockException;
import com.thetechmaddy.ecommerce.models.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.repositories.ProductsRepository;
import com.thetechmaddy.ecommerce.repositories.specifications.ProductsSpecification;
import com.thetechmaddy.ecommerce.services.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Primary
@Service("productsServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    @Override
    public Product getProductById(long productId) {
        return productsRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id : %d", productId)));
    }

    @Override
    public Product checkQuantityAndGetProduct(long productId, int requestedQuantity) {
        Product product = getProductById(productId);
        if (product.hasInsufficientQuantity(requestedQuantity)) {
            throw new InsufficientProductQuantityException(productId, product.getStockQuantity(), requestedQuantity);
        }
        return product;
    }

    @Override
    public Paged<Product> getAllProducts(Integer page, Integer size, String search, ProductFilters productFilters) {
        Specification<Product> specification = new ProductsSpecification(search, productFilters);
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Product> products = this.productsRepository.findAll(specification, pageRequest);
        return new Paged<>(products.getContent(), page, products.getTotalElements(), products.getNumberOfElements());
    }

    @Override
    public void ensureProductInStock(long productId) {
        Product product = getProductById(productId);

        if (!product.isInStock()) {
            throw new ProductOutOfStockException(productId);
        }
    }
}
