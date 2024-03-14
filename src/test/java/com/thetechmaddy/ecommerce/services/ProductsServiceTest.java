package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.Product;
import com.thetechmaddy.ecommerce.exceptions.ProductNotFoundException;
import com.thetechmaddy.ecommerce.models.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.repositories.CategoriesRepository;
import com.thetechmaddy.ecommerce.repositories.ProductsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductsServiceTest {

    @Autowired
    @Qualifier("productsServiceImpl")
    private ProductsService productsService;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    private List<Product> testProducts;

    @BeforeAll
    public void setup() {
        productsRepository.deleteAll();

        List<Product> input = List.of(
                new Product("p1", "pdesc1", new BigDecimal("100.34"), true, categoriesRepository.findByNameIgnoreCase("Electronics")),
                new Product("p2", "pdesc2", new BigDecimal("1000"), true, categoriesRepository.findByNameIgnoreCase("Apparel")),
                new Product("p3", "pdesc3", new BigDecimal("10100.56"), true, categoriesRepository.findByNameIgnoreCase("Apparel")),
                new Product("p4", "pdesc4", new BigDecimal("370.34"), false, categoriesRepository.findByNameIgnoreCase("Home & Garden")),
                new Product("p5", "pdesc5", new BigDecimal("5000.34"), true, categoriesRepository.findByNameIgnoreCase("Home & Garden"))
        );
        testProducts = productsRepository.saveAll(input);
    }

    @Test
    public void testGetSingleProduct_ThrowsProductNotFound() {
        assertThrows(ProductNotFoundException.class, () -> this.productsService.getProductById(1));
    }

    @Test
    public void testGetSingleProduct() {
        Product expected = testProducts.get(0);
        Product actual = this.productsService.getProductById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllProductsNoFilter() {
        Paged<Product> result = this.productsService.getAllProducts(0, 3, "", null);
        assertNotNull(result);

        List<Product> products = result.data();
        assertEquals(3, products.size());
        assertEquals(5, result.total());
        assertEquals("p1", products.get(0).getName());
    }

    @Test
    public void testGetAllProductsWithFilter1() {
        ProductFilters filters = ProductFilters.builder()
                .category("Home & Garden")
                .build();

        Paged<Product> result = this.productsService.getAllProducts(0, 3, "", filters);
        assertNotNull(result);

        List<Product> products = result.data();
        assertEquals(2, products.size());

        assertEquals("p4", products.get(0).getName());
        assertEquals("p5", products.get(1).getName());
    }

    @Test
    public void testGetAllProductsWithFilter2() {
        ProductFilters filters = ProductFilters.builder()
                .category("Home & Garden")
                .available(true)
                .build();

        Paged<Product> result = this.productsService.getAllProducts(0, 3, "", filters);
        assertNotNull(result);

        List<Product> products = result.data();
        assertEquals(1, products.size());

        assertEquals("p5", products.get(0).getName());
        assertEquals(new BigDecimal("5000.34"), products.get(0).getPrice());
    }

    @Test
    public void testGetAllProductsWithFilter3() {
        ProductFilters filters = ProductFilters.builder()
                .minPrice(new BigDecimal("100"))
                .maxPrice(new BigDecimal("1000"))
                .build();

        Paged<Product> result = this.productsService.getAllProducts(0, 5, "", filters);
        assertNotNull(result);

        List<Product> products = result.data();
        assertEquals(3, products.size());

        List<BigDecimal> prices = products.stream()
                .map(Product::getPrice)
                .toList();

        List<BigDecimal> expected = List.of(new BigDecimal("100.34"), new BigDecimal("1000.00"), new BigDecimal("370.34"));
        assertEquals(expected, prices);
    }

    @Test
    public void testGetAllProductsWithSearch() {
        ProductFilters filters = ProductFilters.builder()
                .category("Apparel")
                .build();

        Paged<Product> result = this.productsService.getAllProducts(0, 3, "pdes", filters);
        assertNotNull(result);

        List<Product> products = result.data();
        assertEquals(2, products.size());

        assertEquals("p2", products.get(0).getName());
        assertEquals("p3", products.get(1).getName());
    }
}
