package com.thetechmaddy.ecommerce.repositories.specifications;

import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.models.ProductFilters;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductsSpecification implements Specification<Product> {

    private final String search;
    private final ProductFilters productFilters;

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

        if (productFilters != null) {
            if (productFilters.hasAvailableFilter()) {
                predicates.add(cb.greaterThan(root.get("stockQuantity"), 0));
            }

            if (productFilters.hasCategoryFilter()) {
                predicates.add(cb.equal(root.get("category").get("name"), productFilters.getCategory()));
            }

            Path<BigDecimal> price = root.get("grossAmount");
            if (productFilters.hasMinPriceFilter() && productFilters.hasMaxPriceFilter()) {
                predicates.add(cb.between(price, productFilters.getMinPrice(), productFilters.getMaxPrice()));
            } else if (productFilters.hasMinPriceFilter()) {
                predicates.add(cb.greaterThanOrEqualTo(price, productFilters.getMinPrice()));
            } else if (productFilters.hasMaxPriceFilter()){
                predicates.add(cb.lessThanOrEqualTo(price, productFilters.getMaxPrice()));
            }
        }

        return predicates.isEmpty()
                ? null
                : cb.and(predicates.toArray(new Predicate[0]));
    }
}
