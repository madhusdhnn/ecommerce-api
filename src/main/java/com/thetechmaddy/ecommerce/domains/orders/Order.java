package com.thetechmaddy.ecommerce.domains.orders;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.models.JsonViews.GetOrderResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.OrderInitiateResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.PlaceOrderResponse;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Order extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private OrderStatus status;

    @Setter
    @Column(name = "net_total")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal netTotal;

    @Setter
    @Column(name = "gross_total")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal grossTotal;

    @Builder.Default
    @Setter
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private Payment payment;

    @Setter
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private DeliveryDetails deliveryDetails;

    public Order(long id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }

    public Order(OrderStatus orderStatus, String userId) {
        this.userId = userId;
        this.grossTotal = new BigDecimal("120");
        this.netTotal = new BigDecimal("100");
        this.status = orderStatus;
    }

    public Order(Order order) {
        super(order.getCreatedAt(), order.getUpdatedAt());
        this.userId = order.userId;
        this.status = order.status;
        this.netTotal = order.netTotal.add(new BigDecimal("3455"));
        this.grossTotal = order.grossTotal.add(new BigDecimal("3455"));
        this.orderItems = order.getOrderItems().stream().peek(orderItem -> orderItem.setOrder(this)).collect(Collectors.toList());
    }

    public boolean isPending() {
        return OrderStatus.PENDING == this.status;
    }
}
