package com.thetechmaddy.ecommerce.models;

public class QueryConstants {

    public static final String UPSERT_CART_ITEM_QUERY = """
                INSERT INTO cart_items (product_id, status, quantity, cart_id)
                VALUES (:productId, 'SELECTED', :quantity, :cartId)
                ON CONFLICT (cart_id, product_id)
                DO UPDATE SET quantity = cart_items.quantity + EXCLUDED.quantity
            """;
    public static final String CLEAR_CART_ITEMS_QUERY = """
                DELETE FROM cart_items
                USING carts c
                WHERE c.id = :cartId AND c.user_id = :userId
            """;
    public static final String REMOVE_ITEM_FROM_CART_QUERY = """
                DELETE FROM cart_items ci
                USING carts c
                WHERE c.id = :cartId AND c.user_id = :userId AND ci.product_id = :productId
            """;
    public static final String UPDATE_ORDER_LAST_ACCESS_TIME_QUERY = "UPDATE orders SET updated_at = CURRENT_TIMESTAMP WHERE id = :cartId";
    public static final String IS_CART_UNLOCKED_QUERY = """
                SELECT EXISTS(SELECT 1 FROM carts WHERE id = :cartId AND user_id = :userId AND status = 'UN_LOCKED')
            """;
    public static final String COUNT_CART_ITEMS_BY_USER_CART_QUERY = """
                SELECT COUNT(ci.*) FROM cart_items ci JOIN carts c
                ON c.id = ci.cart_id
                WHERE c.id = :cartId AND c.user_id = :userId
            """;
    public static final String GROSS_TOTAL_FOR_CURRENT_ORDER_QUERY = """
                SELECT SUM(oi.grossAmount) FROM OrderItem oi JOIN Order o ON o.id = oi.order.id
                WHERE o.userId = :userId AND o.status = :orderStatus
            """;
    public static final String GROSS_TOTAL_FOR_SELECTED_CART_ITEMS_QUERY = """
                SELECT SUM(ci.product.grossAmount * ci.quantity) FROM CartItem ci JOIN Cart c ON c.id = ci.cart.id
                WHERE c.id = :cartId AND c.userId = :userId AND ci.status = 'SELECTED'
            """;
    public static final String LOCK_CART_QUERY = """
                UPDATE Cart c SET c.cartStatus = 'LOCKED'
                WHERE c.id = :cartId AND c.userId = :userId
            """;
}
