package com.thetechmaddy.ecommerce.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thetechmaddy.ecommerce.models.validations.ValidationConstants.*;

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
    @Size(min = 1, message = "First Name" + " " + NAME_MIN_LENGTH_MESSAGE)
    private String firstName;

    @NotNull(message = "Last Name" + " " + NOT_NULL_MESSAGE_SUFFIX)
    @Size(min = 1, message = "Last Name" + " " + NAME_MIN_LENGTH_MESSAGE)
    private String lastName;

    public CognitoUser(String cognitoSub) {
        this.cognitoSub = cognitoSub;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
