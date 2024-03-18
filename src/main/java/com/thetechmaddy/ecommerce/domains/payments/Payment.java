package com.thetechmaddy.ecommerce.domains.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.JsonViews.OrderInitiateResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.PaymentStatusResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.PlaceOrderResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.ProcessPaymentResponse;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Payment extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class, PaymentStatusResponse.class})
    private long id;

    @Column(name = "amount")
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class})
    private BigDecimal amount;

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class})
    private PaymentMode paymentMode;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class, PaymentStatusResponse.class})
    private PaymentStatus status;

    @Setter
    @Column(name = "payment_date")
    @JsonView(value = {ProcessPaymentResponse.class, PlaceOrderResponse.class})
    private OffsetDateTime paymentDateTime;

    @Setter
    @EqualsAndHashCode.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    public Payment(PaymentStatus paymentStatus, PaymentMode paymentMode) {
        this.status = paymentStatus;
        this.paymentMode = paymentMode;
        this.amount = new BigDecimal("1000");
    }

    @JsonIgnore
    public boolean isSuccess() {
        return PaymentStatus.SUCCESS == this.status;
    }

    @JsonIgnore
    public boolean isPending() {
        return PaymentStatus.PENDING == this.status;
    }
}
