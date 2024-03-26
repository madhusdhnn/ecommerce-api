package com.thetechmaddy.ecommerce.models;

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
    CANCELLED
}
