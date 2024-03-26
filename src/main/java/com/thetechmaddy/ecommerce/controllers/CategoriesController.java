package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.domains.Category;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.repositories.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoriesController extends AuthenticationAwareController {

    private final CategoriesRepository categoriesRepository;

    @GetMapping
    public ApiResponse<List<Category>> getAllCategories() {
        return ApiResponse.success(this.categoriesRepository.findAll());
    }
}
