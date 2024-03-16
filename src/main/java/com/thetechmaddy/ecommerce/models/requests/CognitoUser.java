package com.thetechmaddy.ecommerce.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.ValidationConstants.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CognitoUser {

    @NotNull(message = "Cognito Sub" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Pattern(regexp = UUID_REGEX, message = "Invalid format for Cognito Sub. The value should be a valid UUID")
    private String cognitoSub;

    @Email
    @NotNull(message = "email" + " " + NOT_NULL_MESSAGE_SUFFIX)
    private String email;

    @NotNull(message = "First Name" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Min(value = 1, message = FIRST_NAME_MIN_LENGTH_MESSAGE)
    private String firstName;

    @NotNull(message = "Last Name" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Min(value = 1, message = LAST_NAME_MIN_LENGTH_MESSAGE)
    private String lastName;

}
