package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.exceptions.UnProcessableEntityException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderUtils {

    public static void ensureOrderInPendingStatus(Order order) {
        Objects.requireNonNull(order, "order == null");

        if (!order.isPending()) {
            throw new UnProcessableEntityException(String.format("Order: (orderId - %d) not in pending status", order.getId()));
        }
    }
}
