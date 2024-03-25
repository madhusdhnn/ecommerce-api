package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.InsufficientProductQuantityException;
import com.thetechmaddy.ecommerce.exceptions.ProductNotFoundException;
import com.thetechmaddy.ecommerce.exceptions.ProductOutOfStockException;
import com.thetechmaddy.ecommerce.models.filters.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProductsServiceTest extends BaseIntegrationTest {

    @Autowired
    @Qualifier("productsServiceImpl")
    private ProductsService productsService;

    @Test
    public void testGetSingleProduct_ThrowsProductNotFound() {
        assertThrows(ProductNotFoundException.class, () -> this.productsService.getProductById(Integer.MAX_VALUE));
    }

    @Test
    public void testGetSingleProduct() {
        Product expected = getTestProducts().get(0);
        Product actual = this.productsService.getProductById(expected.getId());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(0, expected.getTaxPercentage().compareTo(actual.getTaxPercentage()));
        assertEquals(0, expected.getGrossAmount().compareTo(actual.getGrossAmount()));
    }

    @Test
    public void testGetAllProductsNoFilter() {
        Paged<Product> result = this.productsService.getAllProducts(0, 3, "", null);
        assertNotNull(result);

        List<Product> products = result.data();
        assertEquals(3, products.size());
        assertEquals(5, result.total());
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
        assertEquals(new BigDecimal("5000.34"), products.get(0).getUnitPrice());
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

        BigDecimal total = products.stream()
                .map(Product::getGrossAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expected = new BigDecimal("112.38")
                .add(new BigDecimal("414.78"))
                .add(new BigDecimal("896.00"));

        assertEquals(expected, total);
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
    }

    @Test
    public void testEnsureProductStock() {
        Optional<Product> productOptional = getTestProducts().stream().filter(Product::isInStock).findFirst();
        assertTrue(productOptional.isPresent());

        assertDoesNotThrow(() -> this.productsService.ensureProductInStock(productOptional.get().getId()));

        Optional<Product> outOfStockProductOptional = getTestProducts().stream().filter(product -> !product.isInStock()).findFirst();
        assertTrue(outOfStockProductOptional.isPresent());

        assertThrows(ProductOutOfStockException.class, () -> this.productsService.ensureProductInStock(outOfStockProductOptional.get().getId()));
    }

    @Test
    public void testCheckQuantityAndGetProduct() {
        Optional<Product> productOptional = getTestProducts().stream().filter(Product::isInStock).findFirst();
        assertTrue(productOptional.isPresent());

        Product product = productOptional.get();
        assertThrows(InsufficientProductQuantityException.class, () -> productsService.checkQuantityAndGetProduct(product.getId(), product.getStockQuantity() + 3));

        Product actual = productsService.checkQuantityAndGetProduct(product.getId(), product.getStockQuantity() - 1);
        assertNotNull(actual);
        assertEquals(product.getId(), actual.getId());
    }

}
