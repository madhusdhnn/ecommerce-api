package com.thetechmaddy.ecommerce.services;


import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.Cart;
import com.thetechmaddy.ecommerce.domains.CartItem;
import com.thetechmaddy.ecommerce.domains.Product;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.requests.CartItemRequest;
import com.thetechmaddy.ecommerce.models.requests.CartItemUpdateRequest;
import com.thetechmaddy.ecommerce.repositories.CartItemsRepository;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CartsServiceTest extends BaseIntegrationTest {

    private static final String TEST_COGNITO_SUB = "test-cognito-sub";

    @Autowired
    @Qualifier("cartsServiceImpl")
    private CartsService cartsService;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    private long cartId;

    @BeforeEach
    public void setupCart() {
        cartsRepository.deleteAll();
        cartsRepository.flush();

        Cart cart = cartsRepository.save(new Cart(TEST_COGNITO_SUB, UN_LOCKED));
        cartId = cart.getId();
    }

    @Test
    @Transactional
    public void testGetCartEmpty() {
        Cart cart = cartsService.getCart(cartId, TEST_COGNITO_SUB);
        assertNotNull(cart);
        assertEquals(UN_LOCKED, cart.getCartStatus());
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    public void testGetCartNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartsService.getCart(Integer.MAX_VALUE, TEST_COGNITO_SUB));
    }

    @Test
    public void testGetCartNotBelongsToUser() {
        assertThrows(CartNotBelongsToUserException.class, () -> cartsService.getCart(cartId, "some-other-user-cognito-sub"));
    }

    @Test
    public void testAddProductToCartError() {
        assertThrows(ProductNotFoundException.class, () -> cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, new CartItemRequest(Integer.MAX_VALUE, 2)));

        Optional<Product> productOptional = getTestProducts().stream().filter(pr -> !pr.isAvailable()).findFirst();
        assertTrue(productOptional.isPresent());

        Product product = productOptional.get();
        assertThrows(ProductOutOfStockException.class, () -> cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, new CartItemRequest(product.getId(), 3)));

        assertThrows(CartNotFoundException.class, () -> cartsService.addProductToCart(cartId, "some-other-user", new CartItemRequest(getTestProducts().get(2).getId(), 2)));
    }

    @Test
    public void testAddProductToLockedCart() {
        cartsService.lockCart(cartId, TEST_COGNITO_SUB);

        CartItemRequest cartItemRequest = new CartItemRequest(getTestProducts().get(2).getId(), 2);
        assertThrows(CartLockedException.class, () -> cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest));

        cartsService.unlockCart(cartId, TEST_COGNITO_SUB);
    }

    @Test
    public void testAddProductToCart() {
        Product product = getTestProducts().get(2);

        CartItemRequest cartItemRequest = new CartItemRequest(product.getId(), 3);

        // 1. Add item for the first time. Expected: Must be saved
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);
        List<CartItem> items = cartItemsRepository.findAllByCart_IdAndCart_UserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        CartItem cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(3, cartItem.getQuantity());

        // 2. Add the same item. Expected: Just update quantity
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);
        items = cartItemsRepository.findAllByCart_IdAndCart_UserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(6, cartItem.getQuantity());

        // 2. Add different item. Expected: Increase total items count and quantities
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, new CartItemRequest(getTestProducts().get(1).getId(), 2));
        items = cartItemsRepository.findAllByCart_IdAndCart_UserId(cartId, TEST_COGNITO_SUB);
        assertEquals(2, items.size());

        int actualQuantity = items.stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);

        assertEquals(8, actualQuantity);
    }

    @Test
    public void testUpdateProductToCartError() {
        assertThrows(ProductNotFoundException.class, () -> cartsService.updateProductInCart(cartId, Integer.MAX_VALUE, TEST_COGNITO_SUB, new CartItemUpdateRequest(3)));

        Optional<Product> productOptional = getTestProducts().stream().filter(pr -> !pr.isAvailable()).findFirst();
        assertTrue(productOptional.isPresent());

        Product product = productOptional.get();

        assertThrows(ProductOutOfStockException.class, () -> cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, new CartItemUpdateRequest(3)));
        assertThrows(CartNotFoundException.class, () -> cartsService.updateProductInCart(cartId, getTestProducts().get(2).getId(), "some-other-user", new CartItemUpdateRequest(2)));
        assertThrows(ProductNotInCartException.class, () -> cartsService.updateProductInCart(cartId, getTestProducts().get(2).getId(), TEST_COGNITO_SUB, new CartItemUpdateRequest(3)));
    }

    @Test
    public void testUpdateProductToLockedCart() {
        cartsService.lockCart(cartId, TEST_COGNITO_SUB);

        CartItemUpdateRequest cartItemUpdateRequest = new CartItemUpdateRequest(2);
        assertThrows(CartLockedException.class, () -> cartsService.updateProductInCart(cartId, getTestProducts().get(2).getId(), TEST_COGNITO_SUB, cartItemUpdateRequest));

        cartsService.unlockCart(cartId, TEST_COGNITO_SUB);
    }

    @Test
    @Transactional
    public void testUpdateProductToCart() {
        Product product = getTestProducts().get(2);

        cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 2, cartId);

        List<CartItem> items = cartItemsRepository.findAllByCart_IdAndCart_UserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());
        CartItem cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());

        CartItemUpdateRequest cartItemUpdateRequest = new CartItemUpdateRequest(3);
        cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, cartItemUpdateRequest);

        items = cartItemsRepository.findAllByCart_IdAndCart_UserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(3, cartItem.getQuantity());

        Optional<Product> productOptional = getTestProducts().stream().filter(pr -> !pr.isAvailable()).findFirst();
        assertTrue(productOptional.isPresent());

        cartItemsRepository.saveOnConflictUpdateQuantity(productOptional.get().getId(), 2, cartId);

        assertThrows(ProductOutOfStockException.class,
                () -> cartsService.updateProductInCart(cartId, productOptional.get().getId(), TEST_COGNITO_SUB, new CartItemUpdateRequest(3))
        );

        assertTrue(cartItemsRepository.findByCart_IdAndProductId(cartId, productOptional.get().getId()).isEmpty());
    }

    @Test
    public void testRemoveProductFromCartError() {
        Product p1 = getTestProducts().get(1);

        assertThrows(CartNotFoundException.class, () -> cartsService.removeProductFromCart(Integer.MAX_VALUE, p1.getId(), TEST_COGNITO_SUB));

        cartsService.lockCart(cartId, TEST_COGNITO_SUB);
        assertThrows(CartLockedException.class, () -> cartsService.removeProductFromCart(cartId, p1.getId(), TEST_COGNITO_SUB));
        cartsService.unlockCart(cartId, TEST_COGNITO_SUB);
    }

    @Test
    public void testRemoveProductFromCart() {
        Product p1 = getTestProducts().get(1);
        Product p2 = getTestProducts().get(2);

        addItemToCart(p1);

        assertTrue(cartsService.removeProductFromCart(cartId, p1.getId(), TEST_COGNITO_SUB));
        assertFalse(cartsService.removeProductFromCart(cartId, p2.getId(), TEST_COGNITO_SUB));

    }

    @Test
    public void testClearCart() {
        Product product = getTestProducts().get(2);
        CartItemRequest cartItemRequest = new CartItemRequest(product.getId(), 3);
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);

        assertTrue(cartsService.clearCart(cartId, TEST_COGNITO_SUB));
        assertCartUnlocked();

        assertFalse(cartsService.clearCart(cartId, TEST_COGNITO_SUB));
        assertCartUnlocked();

        assertFalse(cartsService.clearCart(Integer.MAX_VALUE, TEST_COGNITO_SUB));
        assertCartUnlocked();
    }

    private void assertCartUnlocked() {
        Optional<Cart> cartOptional = cartsRepository.findByIdAndUserId(cartId, TEST_COGNITO_SUB);
        assertTrue(cartOptional.isPresent());
        assertTrue(cartOptional.get().isUnlocked());
    }

    @Test
    public void testLockCartError() {
        assertThrows(CartNotFoundException.class, () -> cartsService.lockCart(Integer.MAX_VALUE, TEST_COGNITO_SUB));
    }

    @Test
    public void testLockCart() {
        assertTrue(cartsService.lockCart(cartId, TEST_COGNITO_SUB));
        assertFalse(cartsService.lockCart(cartId, TEST_COGNITO_SUB));
    }

    @Test
    public void testUnlockCartError() {
        assertThrows(CartNotFoundException.class, () -> cartsService.unlockCart(Integer.MAX_VALUE, TEST_COGNITO_SUB));
    }

    @Test
    public void testUnlockCart() {
        assertTrue(cartsService.lockCart(cartId, TEST_COGNITO_SUB));
        assertTrue(cartsService.unlockCart(cartId, TEST_COGNITO_SUB));
        assertFalse(cartsService.unlockCart(cartId, TEST_COGNITO_SUB));
    }

    private void addItemToCart(Product product) {
        CartItemRequest cartItemRequest = new CartItemRequest(product.getId(), 2);
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);
    }
}
