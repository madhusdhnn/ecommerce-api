package com.thetechmaddy.ecommerce.models.validations;

import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.thetechmaddy.ecommerce.models.validations.BeanValidator.withValidation;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanValidatorTest {

    private static final String TEST_UUID = UUID.randomUUID().toString();

    @Test
    public void testBeanValidationThrowsEx() {
        assertThrows(ConstraintViolationException.class, () -> withValidation(CognitoUser::new));
        assertThrows(ConstraintViolationException.class, () -> withValidation(() -> new CognitoUser("test", "test", "test", "test")));
    }

    @Test
    public void testBeanValidationSuccess() {
        assertDoesNotThrow(() -> withValidation(() -> new CognitoUser(TEST_UUID, "test@exp.com", "test", "test")));
    }
}
