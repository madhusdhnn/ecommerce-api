package com.thetechmaddy.ecommerce.models;

import com.thetechmaddy.ecommerce.domains.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.NAME_MIN_LENGTH_MESSAGE;
import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.NOT_NULL_MESSAGE_SUFFIX;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {

    @NotNull(message = "name" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Size(min = 1, message = "name" + " " + NAME_MIN_LENGTH_MESSAGE)
    private String name;

    @Email
    @NotNull(message = "email" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private String email;

    @Valid
    @NotNull(message = "shippingAddress" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private Address shippingAddress;

    @Valid
    @NotNull(message = "billingAddress" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private Address billingAddress;
}
