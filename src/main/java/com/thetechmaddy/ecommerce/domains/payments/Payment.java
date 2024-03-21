package com.thetechmaddy.ecommerce.domains.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.Audit;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.JsonViews.*;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payment extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class, PaymentStatusResponse.class, GetOrderResponse.class})
    private long id;

    @Setter
    @Column(name = "amount")
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private BigDecimal amount;

    @Setter
    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private PaymentMode paymentMode;

    @Setter
    @Column(name = "transaction_id")
    @JsonView(value = {ProcessPaymentResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String transactionId;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonView(value = {OrderInitiateResponse.class, ProcessPaymentResponse.class, PlaceOrderResponse.class, PaymentStatusResponse.class, GetOrderResponse.class})
    private PaymentStatus status;

    @Setter
    @Column(name = "payment_date")
    @TimeZoneStorage(value = TimeZoneStorageType.NORMALIZE)
    @JsonView(value = {ProcessPaymentResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
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

    public Payment(long id, PaymentStatus paymentStatus) {
        this.id = id;
        this.status = paymentStatus;
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

    @JsonIgnore
    public boolean isProcessing() {
        return PaymentStatus.PROCESSING == this.status;
    }
}
