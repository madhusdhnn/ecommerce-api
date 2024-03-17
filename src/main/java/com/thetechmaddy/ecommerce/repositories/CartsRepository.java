package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.models.DbConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartsRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findById(long cartId);

    Optional<Cart> findByIdAndUserId(long cartId, String userId);

    @Query(value = DbConstants.IS_CART_UNLOCKED_QUERY, nativeQuery = true)
    boolean isUnlocked(@Param("cartId") long cartId, @Param("userId") String userId);
}
