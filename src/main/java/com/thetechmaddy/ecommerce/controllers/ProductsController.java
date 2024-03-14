package com.thetechmaddy.ecommerce.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.Product;
import com.thetechmaddy.ecommerce.models.JsonViews;
import com.thetechmaddy.ecommerce.models.ProductFilters;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.services.ProductsService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/products")
public class ProductsController extends BaseController {

    private final ProductsService productsService;

    @Autowired
    public ProductsController(@Qualifier("productsServiceImpl") ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/search")
    @JsonView(value = JsonViews.ProductResponse.class)
    public Paged<Product> getAllProducts(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "available", required = false) Boolean available,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice
    ) {
        ProductFilters filters = new ProductFilters(available, category, minPrice, maxPrice);
        return this.productsService.getAllProducts(page, size, search, filters);
    }

    @GetMapping("/{productId}")
    @JsonView(value = JsonViews.ProductResponse.class)
    public ApiResponse<Product> getProductById(@PathParam("productId") long productId) {
        return ApiResponse.success(this.productsService.getProductById(productId));
    }
}
