package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import com.thetechmaddy.ecommerce.models.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

    @Query(value = QueryConstants.GROSS_TOTAL_FOR_CURRENT_ORDER_QUERY)
    BigDecimal getTotal(@Param("userId") String userId, @Param("orderStatus") OrderStatus orderStatus);

}
