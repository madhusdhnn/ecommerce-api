package com.thetechmaddy.ecommerce.domains.orders;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.models.JsonViews.GetOrderResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.OrderInitiateResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.PlaceOrderResponse;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.serializers.BigDecimalToDoubleTwoDecimalPlacesNumberSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends Audit {

    @Id
    @Column(name = "id")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ToString.Exclude
    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Setter
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Setter
    @Column(name = "quantity")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private int quantity;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private OrderItemStatus status;

    @Setter
    @Column(name = "unit_price")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal unitPrice;

    @Setter
    @Column(name = "net_amount")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal netAmount;

    @Setter
    @Column(name = "gross_amount")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal grossAmount;

    @Setter
    @Column(name = "tax_amount")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal taxAmount;

    @Setter
    @Column(name = "tax_percentage")
    @JsonView({OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    @JsonSerialize(using = BigDecimalToDoubleTwoDecimalPlacesNumberSerializer.class)
    private BigDecimal taxPercentage;

}
