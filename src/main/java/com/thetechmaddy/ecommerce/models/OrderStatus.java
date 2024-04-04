package com.thetechmaddy.ecommerce.models;

import java.util.Arrays;

public enum OrderStatus {

    /**
     * Represents checkout in progres for new order
     */
    PENDING,

    /**
     * Represents order has been placed
     */
    CONFIRMED,

    /**
     * Represents seller started processing the order
     */
    PROCESSING,

    /**
     * Represents order has been completed
     */
    COMPLETED,

    /**
     * Represents order cancellation
     */
    CANCELLED;

    public static OrderStatus parse(String name) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unsupported order status name passed - %s", name)));
    }
}
