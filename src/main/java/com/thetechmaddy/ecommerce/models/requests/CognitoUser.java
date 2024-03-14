package com.thetechmaddy.ecommerce.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.ValidationConstants.NOT_NULL_MESSAGE;
import static com.thetechmaddy.ecommerce.models.ValidationConstants.UUID_REGEX;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CognitoUser {

    @NotNull(message = "Cognito Sub" + " " + NOT_NULL_MESSAGE)
    @Pattern(regexp = UUID_REGEX, message = "Invalid format for Cognito Sub. The value should be a valid UUID")
    private String cognitoSub;

    @Email
    @NotNull(message = "email" + " " + NOT_NULL_MESSAGE)
    private String email;

    @NotNull(message = "First Name" + " " + NOT_NULL_MESSAGE)
    @Min(value = 1, message = "First Name must be minimum 1 character length.")
    private String firstName;

    @NotNull(message = "Last Name" + " " + NOT_NULL_MESSAGE)
    @Min(value = 1, message = "Last Name must be minimum 1 character length.")
    private String lastName;

}
