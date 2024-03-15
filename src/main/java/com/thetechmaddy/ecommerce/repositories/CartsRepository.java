package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartsRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findById(long cartId);

    Optional<Cart> findByIdAndUserId(long cartId, String userId);

    @Modifying
    @Query("UPDATE Cart c SET c.cartStatus = 'UN_LOCKED' WHERE c.id = :cartId AND c.userId = :userId AND c.cartStatus = 'LOCKED'")
    int unlockCartIfLocked(@Param("cartId") long cartId, @Param("userId") String userId);
}
