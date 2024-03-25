package com.thetechmaddy.ecommerce.models.filters;

import com.thetechmaddy.ecommerce.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderFilters {

    private Integer year;
    private Boolean last30Days;
    private Boolean past3Months;
    private OrderStatus orderStatus;

    public boolean hasLast30DaysFilter() {
        return Boolean.TRUE.equals(last30Days);
    }

    public boolean hasPast3MonthsFilter() {
        return Boolean.TRUE.equals(past3Months);
    }

    public boolean hasOrderStatusFilter() {
        return orderStatus != null;
    }

    public boolean hasYearFilter() {
        return year != null;
    }

    public static OrderFilters emptyFilters() {
        return OrderFilters.builder().build();
    }
}
