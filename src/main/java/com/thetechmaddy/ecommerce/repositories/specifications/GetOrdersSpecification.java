package com.thetechmaddy.ecommerce.repositories.specifications;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.filters.OrderFilters;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.thetechmaddy.ecommerce.utils.DateTimeUtils.atEndDayOfYear;
import static com.thetechmaddy.ecommerce.utils.DateTimeUtils.atStartDayOfYear;
import static java.util.Objects.requireNonNull;

public class GetOrdersSpecification implements Specification<Order> {

    private final String userId;
    private final OrderFilters filters;

    public GetOrdersSpecification(String userId, OrderFilters filters) {
        requireNonNull(userId, "userId can not be null for GetOrdersSpecification");

        this.userId = userId;
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("userId"), userId));

        if (filters != null) {
            if (filters.hasOrderStatusFilter()) {
                predicates.add(cb.equal(root.get("status"), filters.getOrderStatus()));
            }

            Path<OffsetDateTime> createdAt = root.get("createdAt");

            if (filters.hasYearFilter()) {
                int year = filters.getYear();
                OffsetDateTime startOfYear = atStartDayOfYear(year);
                OffsetDateTime endOfYear = atEndDayOfYear(year);
                predicates.add(cb.between(createdAt, startOfYear, endOfYear));
            }

            if (filters.hasLast30DaysFilter()) {
                OffsetDateTime today = OffsetDateTime.now();
                OffsetDateTime last30Days = today.minusDays(30);
                predicates.add(cb.between(createdAt, today, last30Days));
            }

            if (filters.hasPast3MonthsFilter()) {
                OffsetDateTime today = OffsetDateTime.now();
                OffsetDateTime past3Months = today.minusMonths(3);
                predicates.add(cb.between(createdAt, today, past3Months));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
