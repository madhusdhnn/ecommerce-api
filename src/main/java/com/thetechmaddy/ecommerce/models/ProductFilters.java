package com.thetechmaddy.ecommerce.models;

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

    private Boolean available;
    private String category;
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
        return available != null && available;
    }
}
