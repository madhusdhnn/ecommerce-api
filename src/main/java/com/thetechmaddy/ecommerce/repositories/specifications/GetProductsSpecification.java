package com.thetechmaddy.ecommerce.repositories.specifications;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.models.filters.ProductFilters;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GetProductsSpecification implements Specification<Product> {

    private final String search;
    private final ProductFilters filters;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            predicates.add(cb.or(
                            cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%")
                    )
            );
        }

        if (filters != null) {
            if (filters.hasAvailableFilter()) {
                predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
            }

            if (filters.hasCategoryFilter()) {
                predicates.add(cb.equal(root.get("category").get("name"), filters.getCategory()));
            }

            Path<BigDecimal> price = root.get("grossAmount");
            if (filters.hasMinPriceFilter() && filters.hasMaxPriceFilter()) {
                predicates.add(cb.between(price, filters.getMinPrice(), filters.getMaxPrice()));
            } else if (filters.hasMinPriceFilter()) {
                predicates.add(cb.greaterThanOrEqualTo(price, filters.getMinPrice()));
            } else if (filters.hasMaxPriceFilter()){
                predicates.add(cb.lessThanOrEqualTo(price, filters.getMaxPrice()));
            }
        }

        return predicates.isEmpty()
                ? null
                : cb.and(predicates.toArray(new Predicate[0]));
    }
}
