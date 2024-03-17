package com.thetechmaddy.ecommerce.models;

public enum OrderItemStatus {

    /**
     * Represents item waiting for order confirmation
     */
    PENDING_ORDER_CONFIRMATION,

    /**
     * Represents items are confirmed in the order
     */
    CONFIRMED,
    /**
     * Represents items are packed
     */
    PACKED,
    /**
     * Represents carrier picked up the package
     */
    SHIPPED,

    /**
     * Represents package is out for delivery
     */
    OUT_FOR_DELIVERY,

    /**
     * Represents package has been delivered
     */
    DELIVERED

}
