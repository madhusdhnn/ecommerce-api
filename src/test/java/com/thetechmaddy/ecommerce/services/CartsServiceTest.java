package com.thetechmaddy.ecommerce.services;


import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.products.Product;
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

import static com.thetechmaddy.ecommerce.models.CartItemStatus.SELECTED;
import static com.thetechmaddy.ecommerce.models.CartItemStatus.UN_SELECTED;
import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CartsServiceTest extends BaseIntegrationTest {

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

        Optional<Product> productOptional = getTestProducts().stream().filter(pr -> !pr.isInStock()).findFirst();
        assertTrue(productOptional.isPresent());

        Product product = productOptional.get();
        assertThrows(InsufficientProductQuantityException.class, () -> cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, new CartItemRequest(product.getId(), 3)));

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
    @Transactional
    public void testAddProductToCart() {
        Product product = getTestProducts().get(2);

        CartItemRequest cartItemRequest = new CartItemRequest(product.getId(), 3);

        // 1. Add item for the first time. Expected: Must be saved
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);
        List<CartItem> items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        CartItem cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(3, cartItem.getQuantity());

        // 2. Add the same item. Expected: Just update quantity
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);
        items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(6, cartItem.getQuantity());

        // 2. Add different item. Expected: Increase total items count and quantities
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, new CartItemRequest(getTestProducts().get(1).getId(), 2));
        items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(2, items.size());

        int actualQuantity = items.stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);

        assertEquals(8, actualQuantity);
    }

    @Test
    public void testUpdateProductInCartError() {
        assertThrows(ProductNotFoundException.class, () -> cartsService.updateProductInCart(cartId, Integer.MAX_VALUE, TEST_COGNITO_SUB, new CartItemUpdateRequest(3)));

        Optional<Product> productOptional = getTestProducts().stream().filter(pr -> !pr.isInStock()).findFirst();
        assertTrue(productOptional.isPresent());

        Product product = productOptional.get();

        assertThrows(ProductOutOfStockException.class, () -> cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, new CartItemUpdateRequest(3)));
        assertThrows(CartNotFoundException.class, () -> cartsService.updateProductInCart(cartId, getTestProducts().get(2).getId(), "some-other-user", new CartItemUpdateRequest(2)));
        assertThrows(ProductNotInCartException.class, () -> cartsService.updateProductInCart(cartId, getTestProducts().get(2).getId(), TEST_COGNITO_SUB, new CartItemUpdateRequest(3)));
    }

    @Test
    public void testUpdateProductInLockedCart() {
        cartsService.lockCart(cartId, TEST_COGNITO_SUB);

        CartItemUpdateRequest cartItemUpdateRequest = new CartItemUpdateRequest(2);
        assertThrows(CartLockedException.class, () -> cartsService.updateProductInCart(cartId, getTestProducts().get(2).getId(), TEST_COGNITO_SUB, cartItemUpdateRequest));

        cartsService.unlockCart(cartId, TEST_COGNITO_SUB);
    }

    @Test
    @Transactional
    public void testUpdateProductQuantityInCart() {
        Product product = getTestProducts().get(2);

        cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 2, cartId);

        // Before
        List<CartItem> items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());
        CartItem cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());

        CartItemUpdateRequest cartItemUpdateRequest = new CartItemUpdateRequest(null);
        cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, cartItemUpdateRequest);

        // No Change
        items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());

        // Updated
        cartItemUpdateRequest = new CartItemUpdateRequest(3);
        cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, cartItemUpdateRequest);

        items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(3, cartItem.getQuantity());
    }

    @Test
    @Transactional
    public void testUpdateProductSelectionInCart() {
        Product product = getTestProducts().get(2);

        cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 2, cartId);

        CartItemUpdateRequest cartItemUpdateRequest = new CartItemUpdateRequest(false);
        cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, cartItemUpdateRequest);

        List<CartItem> items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        CartItem cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(UN_SELECTED, cartItem.getStatus());

        cartItemUpdateRequest = new CartItemUpdateRequest(true);
        cartsService.updateProductInCart(cartId, product.getId(), TEST_COGNITO_SUB, cartItemUpdateRequest);

        items = cartItemsRepository.findAllByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB);
        assertEquals(1, items.size());

        cartItem = items.get(0);
        assertNotNull(cartItem);
        assertEquals(SELECTED, cartItem.getStatus());
    }

    @Test
    @Transactional
    public void testUpdateProductInCartRemoveIfOutOfStock() {
        Optional<Product> productOptional = getTestProducts().stream().filter(pr -> !pr.isInStock()).findFirst();
        assertTrue(productOptional.isPresent());

        cartItemsRepository.saveOnConflictUpdateQuantity(productOptional.get().getId(), 2, cartId);

        assertThrows(ProductOutOfStockException.class,
                () -> cartsService.updateProductInCart(cartId, productOptional.get().getId(), TEST_COGNITO_SUB, new CartItemUpdateRequest(3))
        );

        assertTrue(cartItemsRepository.findByCartIdAndProductId(cartId, productOptional.get().getId()).isEmpty());
    }

    @Test
    public void testRemoveProductFromCartError() {
        Product p1 = getTestProducts().get(1);

        cartsService.lockCart(cartId, TEST_COGNITO_SUB);
        assertThrows(CartLockedException.class, () -> cartsService.removeProductFromCart(cartId, p1.getId(), TEST_COGNITO_SUB));
        cartsService.unlockCart(cartId, TEST_COGNITO_SUB);
    }

    @Test
    @Transactional
    public void testRemoveProductFromCart() {
        Product p1 = getTestProducts().get(1);
        Product p2 = getTestProducts().get(2);

        addItemToCart(p1);

        assertTrue(cartsService.removeProductFromCart(cartId, p1.getId(), TEST_COGNITO_SUB));
        assertFalse(cartsService.removeProductFromCart(cartId, p2.getId(), TEST_COGNITO_SUB));

    }

    @Test
    public void testClearCart() {
        assertThrows(CartNotFoundException.class, () -> cartsService.clearCart(Integer.MAX_VALUE, "some-other-user"));

        cartsService.lockCart(cartId, TEST_COGNITO_SUB);
        assertThrows(CartLockedException.class, () -> cartsService.clearCart(cartId, TEST_COGNITO_SUB));

        cartsService.unlockCart(cartId, TEST_COGNITO_SUB);
        assertCartUnlocked();

        Product product = getTestProducts().get(2);
        CartItemRequest cartItemRequest = new CartItemRequest(product.getId(), 3);
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);

        assertTrue(cartsService.clearCart(cartId, TEST_COGNITO_SUB));
        assertCartUnlocked();

        // try to clear already empty cart
        assertFalse(cartsService.clearCart(cartId, TEST_COGNITO_SUB));
        assertCartUnlocked();
    }

    @Test
    public void testLockCart() {
        assertTrue(cartsService.lockCart(cartId, TEST_COGNITO_SUB));
    }

    @Test
    public void testUnlockCart() {
        cartsService.lockCart(cartId, TEST_COGNITO_SUB);
        assertTrue(cartsService.unlockCart(cartId, TEST_COGNITO_SUB));
    }

    @Test
    public void testGetUserCart() {
        Cart cart = cartsService.getUserCart(TEST_COGNITO_SUB);
        assertNotNull(cart);
        assertEquals(TEST_COGNITO_SUB, cart.getUserId());

        assertThrows(CartNotFoundException.class, () -> cartsService.getUserCart("some-other-user"));
    }

    private void addItemToCart(Product product) {
        CartItemRequest cartItemRequest = new CartItemRequest(product.getId(), 2);
        cartsService.addProductToCart(cartId, TEST_COGNITO_SUB, cartItemRequest);
    }

    private void assertCartUnlocked() {
        Optional<Cart> cartOptional = cartsRepository.findByIdAndUserId(cartId, TEST_COGNITO_SUB);
        assertTrue(cartOptional.isPresent());
        assertTrue(cartOptional.get().isUnlocked());
    }
}
