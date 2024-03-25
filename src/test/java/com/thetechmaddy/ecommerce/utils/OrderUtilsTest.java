package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.exceptions.UnProcessableEntityException;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.thetechmaddy.ecommerce.models.OrderStatus.*;
import static com.thetechmaddy.ecommerce.utils.OrderUtils.ensureOrderInPendingStatus;
import static org.junit.jupiter.api.Assertions.*;

public class OrderUtilsTest {

    @Test
    public void testOrderPendingStatusError() {
        List<OrderStatus> statusList = List.of(PROCESSING, CONFIRMED, COMPLETED, CANCELLED);
        for (OrderStatus orderStatus : statusList) {
            Order order = new Order(1, orderStatus);
            String message = String.format("Order: (orderId - %d) not in pending status", order.getId());
            UnProcessableEntityException ex = assertThrows(UnProcessableEntityException.class,
                    () -> ensureOrderInPendingStatus(order));
            assertEquals(message, ex.getMessage());
        }
    }

    @Test
    public void testOrderPendingStatusSuccess() {
        Order order = new Order(1, PENDING);
        assertDoesNotThrow(() -> ensureOrderInPendingStatus(order));
    }
}
