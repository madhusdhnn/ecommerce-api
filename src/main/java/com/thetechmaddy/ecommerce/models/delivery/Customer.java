package com.thetechmaddy.ecommerce.models.delivery;

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
public class Customer {

    @NotNull(message = "name" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Size(min = 1, message = "name" + " " + NAME_MIN_LENGTH_MESSAGE)
    private String name;

    @Email
    @NotNull(message = "email" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private String email;
}
