package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

}
