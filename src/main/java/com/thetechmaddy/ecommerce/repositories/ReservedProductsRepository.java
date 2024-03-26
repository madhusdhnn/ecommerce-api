package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.products.ReservedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedProductsRepository extends JpaRepository<ReservedProduct, Long> {

    List<ReservedProduct> findAllByOrderIdAndProductIdIn(long orderId, List<Long> productIds);

    @Modifying
    @Query(value = "DELETE FROM ReservedProduct rp WHERE rp.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") long orderId);

    long countByOrderId(long orderId);
}
