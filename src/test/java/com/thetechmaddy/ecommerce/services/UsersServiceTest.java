package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.users.User;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsersServiceTest extends BaseIntegrationTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @BeforeEach
    public void clearAll() {
        cartsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    public void testProvisionUser() {
        CognitoUser testUser = buildTestUser();
        User user = usersService.provisionUser(testUser);

        assertNotNull(user);
        assertEquals(testUser.getCognitoSub(), user.getCognitoSub());

        assertTrue(cartsRepository.findByUserId(user.getCognitoSub()).isPresent());
    }

    @Test
    public void testProvisionUserError() {
        CognitoUser testUser = buildTestUser();
        usersService.provisionUser(testUser);

        assertThrows(DataIntegrityViolationException.class, () -> usersService.provisionUser(testUser));
    }

    private static CognitoUser buildTestUser() {
        return CognitoUser.builder()
                .email("test@example.com")
                .cognitoSub(UUID.randomUUID().toString())
                .firstName("Test")
                .lastName("User")
                .build();
    }
}
