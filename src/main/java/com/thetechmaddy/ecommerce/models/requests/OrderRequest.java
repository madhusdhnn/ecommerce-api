package com.thetechmaddy.ecommerce.models.requests;

import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.NOT_NULL_MESSAGE_SUFFIX;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private long cartId;

    @Valid
    @NotNull(message = "deliveryInfo" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private DeliveryInfo deliveryInfo;

    @Valid
    @NotNull(message = "paymentInfo" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private PaymentInfo paymentInfo;

}
