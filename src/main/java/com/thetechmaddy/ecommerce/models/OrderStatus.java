package com.thetechmaddy.ecommerce.models;

public enum OrderStatus {

    /**
     * Represents checkout in progres for new order
     */
    PENDING,

    /**
     * Represents seller started processing the order
     */
    PROCESSING,

    /**
     * Represents order has been placed
     */
    CONFIRMED,

    /**
     * Represents order has been completed. This includes the return window as well
     */
    COMPLETED,

    /**
     * Represents order cancellation
     */
    CANCELLED,

    /**
     * Reprents the pending order was cancelled due to change in order items or user navigated away
     */
    DRAFT_CANCELLED

}
