package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.Address;
import com.thetechmaddy.ecommerce.models.delivery.Customer;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TestUtils {

    public static OrderRequest getOrderRequest(long cartId, TestOrderRequestOptions options) {
        DeliveryInfo deliveryInfo = null;
        PaymentInfo paymentInfo = null;

        if (options.isCreateDeliveryInfo()) {
            Address shippingAddress = Address.builder()
                    .addressOne("Wall St")
                    .zipCode("567234")
                    .build();

            Address billingAddress = Address.builder()
                    .addressOne("Journal St")
                    .zipCode("567244")
                    .build();

            deliveryInfo = new DeliveryInfo(
                    new Customer("Jane Doe", "jane.doe@example.com"),
                    options.isShippingSameAsBilling() ? billingAddress : shippingAddress,
                    billingAddress
            );
        }

        if (options.isCreatePaymentInfo()) {
            paymentInfo = new PaymentInfo(options.getCartAmount(), options.getPaymentMode());
        }

        return new OrderRequest(cartId, deliveryInfo, paymentInfo);
    }

    @Getter
    @Builder
    public static class TestOrderRequestOptions {
        private boolean createDeliveryInfo;
        private boolean shippingSameAsBilling;
        private boolean createPaymentInfo;
        private BigDecimal cartAmount;
        private PaymentMode paymentMode;
    }
}
