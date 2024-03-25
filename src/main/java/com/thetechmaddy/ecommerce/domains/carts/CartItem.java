package com.thetechmaddy.ecommerce.domains.carts;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.models.JsonViews;
import com.thetechmaddy.ecommerce.models.carts.CartItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends Audit {

    @Id
    @Column(name = "id")
    @JsonView(JsonViews.CartResponse.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Product.class, optional = false)
    @JsonView(JsonViews.CartResponse.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @JsonView(JsonViews.CartResponse.class)
    @Column(name = "quantity")
    private int quantity;

    @Setter
    @JsonView(JsonViews.CartResponse.class)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CartItemStatus status;

    @Setter
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Cart.class)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public CartItem(Product product, int quantity, CartItemStatus status, Cart cart) {
        super(OffsetDateTime.now(), OffsetDateTime.now());
        this.product = product;
        this.quantity = quantity;
        this.status = status;
        this.cart = cart;
    }

    public CartItem(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public void incrementQuantity(int quantity) {
        this.quantity += quantity;
    }
}
