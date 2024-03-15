package com.thetechmaddy.ecommerce.domains;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.models.CartStatus;
import com.thetechmaddy.ecommerce.models.JsonViews.CartResponse;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalTwoDecimalPlacesSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.thetechmaddy.ecommerce.models.CartStatus.LOCKED;
import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cart extends Timestamp {

    @Id
    @Column(name = "id")
    @JsonView(CartResponse.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "status")
    @JsonView(CartResponse.class)
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
    @JsonView(CartResponse.class)
    private Set<CartItem> cartItems = new HashSet<>();

    @Transient
    @EqualsAndHashCode.Exclude
    @JsonView(CartResponse.class)
    @JsonSerialize(using = BigDecimalTwoDecimalPlacesSerializer.class)
    private BigDecimal subTotal = new BigDecimal("0.0");

    public Cart(String userId, CartStatus cartStatus) {
        super(OffsetDateTime.now(), OffsetDateTime.now());
        this.userId = userId;
        this.cartStatus = cartStatus;
        this.cartItems = new HashSet<>();
    }

    public boolean belongsTo(String userId) {
        return Objects.equals(this.userId, userId);
    }

    public void calculateSubTotal() {
        this.subTotal = this.cartItems.stream()
                .filter(CartItem::isSelected)
                .map(ci -> ci.getProduct().getPrice().multiply(new BigDecimal(ci.getQuantity())))
                .reduce(new BigDecimal("0.0"), BigDecimal::add);
    }

    public boolean isLocked() {
        return LOCKED.equals(cartStatus);
    }

    public void lock() {
        this.cartStatus = LOCKED;
    }

    public void unlock() {
        this.cartStatus = UN_LOCKED;
    }

    public boolean isUnlocked() {
        return UN_LOCKED.equals(cartStatus);
    }
}
