package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByUserIdAndStatusEquals(String userId, OrderStatus orderStatus);

    Optional<Order> findByIdAndUserId(long orderId, String userId);

    long countByUserIdAndStatusEquals(String userId, OrderStatus orderStatus);

    List<Order> findByStatusEquals(OrderStatus orderStatus);
}
