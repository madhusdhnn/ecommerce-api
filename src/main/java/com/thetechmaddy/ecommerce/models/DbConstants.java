package com.thetechmaddy.ecommerce.models;

public class DbConstants {

    public static final String UN_LOCK_CARTIF_LOCKED_QUERY = """
                UPDATE Cart c SET c.cartStatus = 'UN_LOCKED'
                WHERE c.id = :cartId AND c.userId = :userId AND c.cartStatus = 'LOCKED'
            """;
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
    public static final String ORDER_SUMMARY_GROUP_BY_TAX_PERCENTAGE_QUERY = """
            SELECT new com.thetechmaddy.ecommerce.models.CheckoutData(
                SUM(ci.quantity * ci.product.unitPrice),
                SUM(ci.quantity * ci.product.grossAmount),
                SUM(ci.product.taxAmount),
                ci.product.taxPercentage)
            FROM CartItem ci
            WHERE ci.cart.id = :cartId AND ci.cart.userId = :userId AND ci.status = :status
            GROUP BY ci.product.taxPercentage
            """;
    public static final String ORDER_SUMMARY_QUERY = """
            SELECT new com.thetechmaddy.ecommerce.models.CheckoutData(
                SUM(ci.quantity * ci.product.unitPrice),
                SUM(ci.quantity * ci.product.grossAmount))
            FROM CartItem ci
            WHERE ci.cart.id = :cartId AND ci.cart.userId = :userId AND ci.status = :status
            """;
}
