package com.thetechmaddy.ecommerce.repositories.specifications;

import com.thetechmaddy.ecommerce.domains.Product;
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
                predicates.add(cb.equal(root.get("available"), productFilters.getAvailable()));
            }

            if (productFilters.hasCategoryFilter()) {
                predicates.add(cb.equal(root.get("category").get("name"), productFilters.getCategory()));
            }

            if (productFilters.hasMinPriceFilter()) {
                Path<BigDecimal> price = root.get("price");
                if (productFilters.hasMaxPriceFilter()) {
                    predicates.add(cb.between(price, productFilters.getMinPrice(), productFilters.getMaxPrice()));
                } else {
                    predicates.add(cb.greaterThanOrEqualTo(price, productFilters.getMinPrice()));
                }
            }

            if (productFilters.hasMaxPriceFilter()) {
                Path<BigDecimal> price = root.get("price");
                if (productFilters.hasMinPriceFilter()) {
                    predicates.add(cb.between(price, productFilters.getMinPrice(), productFilters.getMaxPrice()));
                } else {
                    predicates.add(cb.lessThanOrEqualTo(price, productFilters.getMaxPrice()));
                }
            }
        }

        return predicates.isEmpty()
                ? null
                : cb.and(predicates.toArray(new Predicate[0]));
    }
}
