package com.thetechmaddy.ecommerce.models.delivery;

import com.thetechmaddy.ecommerce.domains.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.NOT_NULL_MESSAGE_SUFFIX;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {

    @Valid
    @NotNull(message = "customer" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private Customer customer;

    @Valid
    @NotNull(message = "shippingAddress" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private Address shippingAddress;

    @Valid
    @NotNull(message = "billingAddress" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private Address billingAddress;
}
