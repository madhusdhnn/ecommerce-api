package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.thetechmaddy.ecommerce.models.DbConstants.UPDATE_ORDER_LAST_ACCESS_TIME_QUERY;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByUserIdAndStatusEquals(String userId, OrderStatus orderStatus);

    @Transactional
    @Modifying
    @Query(value = UPDATE_ORDER_LAST_ACCESS_TIME_QUERY, nativeQuery = true)
    void updateTime(@Param("cartId") long id);

    long countByUserIdAndStatusEquals(String userId, OrderStatus orderStatus);
}
