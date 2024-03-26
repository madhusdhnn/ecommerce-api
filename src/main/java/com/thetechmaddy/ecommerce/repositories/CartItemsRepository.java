package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.QueryConstants.*;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, Long> {

    /**
     * used in testing
     */
    @Transactional
    @Modifying
    @Query(value = UPSERT_CART_ITEM_QUERY, nativeQuery = true)
    void saveOnConflictUpdateQuantity(@Param("productId") long productId, @Param("quantity") int quantity, @Param("cartId") long cartId);

    Optional<CartItem> findByCartIdAndProductId(long cartId, long productId);

    @Modifying
    @Query(value = CLEAR_CART_ITEMS_QUERY, nativeQuery = true)
    int clearCartItems(@Param("cartId") long cartId, @Param("userId") String userId);

    @Modifying
    @Query(value = REMOVE_ITEM_FROM_CART_QUERY, nativeQuery = true)
    int removeItem(@Param("cartId") long cartId, @Param("productId") long productId, @Param("userId") String userId);

    /**
     * used in testing
     */
    List<CartItem> findAllByCartIdAndCartUserId(long cartId, String userId);

    /**
     * used in testing
     */
    @Query(value = COUNT_CART_ITEMS_BY_USER_CART_QUERY, nativeQuery = true)
    int countByCartIdAndCartUserId(@Param("cartId") long cartId, @Param("userId") String userId);

    /**
     * used in testing
     */
    @Query(value = GROSS_TOTAL_FOR_SELECTED_CART_ITEMS_QUERY)
    BigDecimal getGrossTotalForSelectedCartItems(@Param("cartId") long cartId, @Param("userId") String userId);
}