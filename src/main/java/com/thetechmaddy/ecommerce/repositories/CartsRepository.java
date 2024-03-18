package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.QueryConstants.IS_CART_UNLOCKED_QUERY;

@Repository
public interface CartsRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(value = "Cart.cartItems")
    Optional<Cart> findById(long cartId);

    @EntityGraph(value = "Cart.cartItems")
    Optional<Cart> findByIdAndUserId(long cartId, String userId);

    @Query(value = IS_CART_UNLOCKED_QUERY, nativeQuery = true)
    boolean isUnlocked(@Param("cartId") long cartId, @Param("userId") String userId);

    @EntityGraph(value = "Cart.cartItems")
    Optional<Cart> findByUserId(String userId);
}
