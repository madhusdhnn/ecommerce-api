package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.thetechmaddy.ecommerce.BaseIntegrationTest.TEST_COGNITO_SUB;
import static com.thetechmaddy.ecommerce.models.carts.CartStatus.UN_LOCKED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CartLockApplierServiceTest {

    @Autowired
    @Qualifier("cartLockApplierServiceImpl")
    private CartLockApplierService cartLockApplierService;

    @Autowired
    private CartsRepository cartsRepository;

    private long cartId;

    @BeforeEach
    public void setupCart() {
        cartsRepository.deleteAll();
        cartsRepository.flush();

        Cart cart = cartsRepository.save(new Cart(TEST_COGNITO_SUB, UN_LOCKED));
        cartId = cart.getId();
    }

    @Test
    public void testForNPE() {
        assertThrows(NullPointerException.class, () -> cartLockApplierService.acquireLock(null, "test reason"));
        assertThrows(NullPointerException.class, () -> cartLockApplierService.releaseLock(null, "test reason"));
    }

    @Test
    public void testAcquireLock() {
        Optional<Cart> cartOptional = cartsRepository.findById(cartId);
        assertTrue(cartOptional.isPresent());

        Cart cart = cartOptional.get();
        assertTrue(cart.isUnlocked());

        cartLockApplierService.acquireLock(cart, "test reason");

        assertTrue(cartsRepository.findById(cartId).filter(Cart::isLocked).isPresent());
    }

    @Test
    public void testReleaseLock() {
        Optional<Cart> cartOptional = cartsRepository.findById(cartId);
        assertTrue(cartOptional.isPresent());

        Cart cart = cartOptional.get();
        cartLockApplierService.acquireLock(cart, "test reason");

        cartLockApplierService.releaseLock(cart, "test reason");

        assertTrue(cartsRepository.findById(cartId).filter(Cart::isUnlocked).isPresent());
    }
}
