package com.thetechmaddy.ecommerce;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.repositories.*;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIntegrationTest {

    public static final String TEST_COGNITO_SUB = "test-cognito-sub";

    @Getter
    private List<Product> testProducts;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ReservedProductsRepository reservedProductsRepository;

    @BeforeAll
    public void setup() {
        cartsRepository.deleteAll();
        reservedProductsRepository.deleteAll();
        ordersRepository.deleteAll();
        productsRepository.deleteAll();

        List<Product> input = List.of(
                new Product("p1", "pdesc1", new BigDecimal("100.34"), true, categoriesRepository.findByNameIgnoreCase("Electronics")),
                new Product("p2", "pdesc2", new BigDecimal("800"), true, categoriesRepository.findByNameIgnoreCase("Apparel")),
                new Product("p3", "pdesc3", new BigDecimal("10100.56"), true, categoriesRepository.findByNameIgnoreCase("Apparel")),
                new Product("p4", "pdesc4", new BigDecimal("370.34"), false, categoriesRepository.findByNameIgnoreCase("Home & Garden")),
                new Product("p5", "pdesc5", new BigDecimal("5000.34"), true, categoriesRepository.findByNameIgnoreCase("Home & Garden"))
        );
        testProducts = productsRepository.saveAll(input);
    }
}
