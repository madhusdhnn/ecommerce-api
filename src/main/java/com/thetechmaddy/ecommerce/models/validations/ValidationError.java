package com.thetechmaddy.ecommerce.models.validations;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ValidationError {

    private String field;
    private String message;
    private Object invalidValue;
}
