package com.thetechmaddy.ecommerce.domains;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.models.JsonViews.GetOrderResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.OrderInitiateResponse;
import com.thetechmaddy.ecommerce.models.JsonViews.PlaceOrderResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeliveryDetails extends Audit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private long id;

    @EqualsAndHashCode.Exclude
    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "customer_name")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String customerName;

    @Column(name = "customer_email")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String customerEmail;

    @Column(name = "shipping_address_1")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String shippingAddressOne;

    @Column(name = "shipping_address_2")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String shippingAddressTwo;

    @Column(name = "shipping_city")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String shippingCity;

    @Column(name = "shipping_state")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String shippingState;

    @Column(name = "shipping_zip_code")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String shippingZipCode;

    @Column(name = "billing_address_1")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String billingAddressOne;

    @Column(name = "billing_address_2")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String billingAddressTwo;

    @Column(name = "billing_city")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String billingCity;

    @Column(name = "billing_state")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String billingState;

    @Column(name = "billing_zip_code")
    @JsonView(value = {OrderInitiateResponse.class, PlaceOrderResponse.class, GetOrderResponse.class})
    private String billingZipCode;
}
