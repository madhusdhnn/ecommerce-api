package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Query(value = "INSERT INTO cart_items (product_id, status, quantity, cart_id) VALUES (:productId, 'SELECTED', :quantity, :cartId) " +
            "ON CONFLICT (cart_id, product_id) " +
            "DO UPDATE SET quantity = cart_items.quantity + EXCLUDED.quantity", nativeQuery = true
    )
    int saveOnConflictUpdateQuantity(@Param("productId") long productId, @Param("quantity") int quantity, @Param("cartId") long cartId);

    Optional<CartItem> findByCart_IdAndProductId(long cartId, long productId);

    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE cart_id = (SELECT id as cart_id FROM carts WHERE id = :cartId AND user_id = :userId)", nativeQuery = true)
    int clearCartItems(@Param("cartId") long cartId, @Param("userId") String userId);

    @Modifying
    @Query(value = "DELETE FROM cart_items WHERE cart_id = (SELECT id as cart_id FROM carts WHERE id = :cartId AND user_id = :userId) AND product_id = :productId", nativeQuery = true)
    int removeItem(@Param("cartId") long cartId, @Param("productId") long productId, @Param("userId") String userId);

    List<CartItem> findAllByCart_IdAndCart_UserId(long cartId, String userId);
}