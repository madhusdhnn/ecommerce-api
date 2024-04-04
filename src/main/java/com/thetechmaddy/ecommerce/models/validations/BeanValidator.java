package com.thetechmaddy.ecommerce.models.validations;

import jakarta.validation.*;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanValidator {

    public static <T> T withValidation(Supplier<T> beanSupplier) {
        T bean = beanSupplier.get();
        return withValidator(validator -> {
            Set<ConstraintViolation<T>> violations = validator.validate(bean);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return bean;
        });
    }

    private static <T> T withValidator(Function<Validator, T> action) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            return action.apply(validator);
        }
    }
}
