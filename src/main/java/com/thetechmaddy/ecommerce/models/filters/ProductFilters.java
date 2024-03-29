package com.thetechmaddy.ecommerce.models.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductFilters {

    private String category;
    private Boolean available;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public boolean hasCategoryFilter() {
        return category != null && !category.isBlank();
    }

    public boolean hasMinPriceFilter() {
        return minPrice != null;
    }

    public boolean hasMaxPriceFilter() {
        return maxPrice != null;
    }

    public boolean hasAvailableFilter() {
        return Boolean.TRUE.equals(available);
    }
}
