package com.thetechmaddy.ecommerce.domains.carts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.models.JsonViews.CartResponse;
import com.thetechmaddy.ecommerce.models.carts.CartStatus;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.thetechmaddy.ecommerce.models.carts.CartStatus.LOCKED;
import static com.thetechmaddy.ecommerce.models.carts.CartStatus.UN_LOCKED;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "Cart.cartItems", attributeNodes = @NamedAttributeNode("cartItems"))
public class Cart extends Audit {

    @Id
    @Column(name = "id")
    @JsonView(CartResponse.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Setter
    @Column(name = "status")
    @JsonView(CartResponse.class)
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
    @JsonView(CartResponse.class)
    private List<CartItem> cartItems = new ArrayList<>();

    @Setter
    @Transient
    @EqualsAndHashCode.Exclude
    @JsonView(CartResponse.class)
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Setter
    @Transient
    @EqualsAndHashCode.Exclude
    @JsonView(CartResponse.class)
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal taxTotal = BigDecimal.ZERO;

    @Setter
    @Transient
    @EqualsAndHashCode.Exclude
    @JsonView(CartResponse.class)
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal grossTotal = BigDecimal.ZERO;

    public Cart(String userId, CartStatus cartStatus) {
        super(OffsetDateTime.now(), OffsetDateTime.now());
        this.userId = userId;
        this.cartStatus = cartStatus;
        this.cartItems = new ArrayList<>();
    }

    public Cart(BigDecimal grossTotal) {
        this.grossTotal = grossTotal;
    }

    @JsonIgnore
    public boolean belongsTo(String userId) {
        return Objects.equals(this.userId, userId);
    }

    @JsonIgnore
    public boolean isLocked() {
        return LOCKED.equals(cartStatus);
    }

    public void lock() {
        this.cartStatus = LOCKED;
    }

    public void unlock() {
        this.cartStatus = UN_LOCKED;
    }

    @JsonIgnore
    public boolean isUnlocked() {
        return UN_LOCKED.equals(cartStatus);
    }

    public void addProduct(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

}
