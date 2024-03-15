package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.Cart;
import com.thetechmaddy.ecommerce.domains.CartItem;
import com.thetechmaddy.ecommerce.domains.Product;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.requests.CartItemRequest;
import com.thetechmaddy.ecommerce.models.requests.CartItemUpdateRequest;
import com.thetechmaddy.ecommerce.repositories.CartItemsRepository;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.repositories.ProductsRepository;
import com.thetechmaddy.ecommerce.services.CartsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Primary
@Service("cartsServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartsServiceImpl implements CartsService {

    private final CartsRepository cartsRepository;
    private final ProductsRepository productsRepository;
    private final CartItemsRepository cartItemsRepository;

    @Override
    public Cart getCart(long cartId, String userId) {
        Cart cart = this.cartsRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart not found with id %d", cartId)));

        if (!cart.belongsTo(userId)) {
            throw new CartNotBelongsToUserException(String.format("Cart with id %d does not belong to the user - %s", cartId, userId));
        }

        cart.calculateSubTotal();
        return cart;
    }

    @Override
    @Transactional
    public void addProductToCart(long cartId, String userId, CartItemRequest cartItemRequest) {
        long productId = cartItemRequest.getProductId();

        ensureProductExistsAndAlsoInStock(productId);
        this.ensureCartExistsAndNotLocked(cartId, userId);

        int rowsAffected = this.cartItemsRepository.saveOnConflictUpdateQuantity(productId, cartItemRequest.getQuantity(), cartId);
        log.info(String.format("Product: (productId - %d) added/ updated in the cart: (cartId - %d) by the user: (userId - %s). Affected rows: %d", productId, cartId, userId, rowsAffected));
    }

    @Override
    @Transactional
    public void updateProductInCart(long cartId, long productId, String userId, CartItemUpdateRequest cartItemUpdateRequest) {
        try {
            ensureProductExistsAndAlsoInStock(productId);
            this.ensureCartExistsAndNotLocked(cartId, userId);

            CartItem cartItem = this.cartItemsRepository.findByCart_IdAndProductId(cartId, productId)
                    .orElseThrow(() -> new ProductNotInCartException(productId));

            cartItem.setQuantity(cartItemUpdateRequest.getQuantity());

            this.cartItemsRepository.save(cartItem);
        } catch (ProductOutOfStockException ex) {
            boolean removed = this.removeProductFromCart(cartId, productId, userId);
            if (removed) {
                log.info(String.format("Product: (productId - %d) removed from cart as it went out of stock", productId));
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public boolean removeProductFromCart(long cartId, long productId, String userId) {
        this.ensureCartExistsAndNotLocked(cartId, userId);

        int rowsDeleted = this.cartItemsRepository.removeItem(cartId, productId, userId);
        if (rowsDeleted > 0) {
            log.info(String.format("Deleted product: (productId - %d) from cart: (cartId - %d) by user: (userId - %s)", productId, cartId, userId));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean clearCart(long cartId, String userId) {
        int rowsUpdated = this.cartsRepository.unlockCartIfLocked(cartId, userId);
        if (rowsUpdated > 0) {
            log.info(String.format("Cart: (cartId - %d) unlocked by user: (userId - %s) due to clear cart request.", cartId, userId));
        }

        int rowsDeleted = this.cartItemsRepository.clearCartItems(cartId, userId);
        if (rowsDeleted > 0) {
            log.info(String.format("Cart: (cartId - %d) cleared by user: (userId - %s).", cartId, userId));
            return true;
        }
        return false;
    }

    @Override
    public boolean lockCart(long cartId, String userId) {
        log.info(String.format("Cart lock requested by userId %s for cartId %d", userId, cartId));

        Cart cart = cartsRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart not found: (cartId - %d)", cartId)));

        if (cart.isLocked()) {
            log.info(String.format("Cart already locked: (cartId - %d). Skipping..", cartId));
            return false;
        }

        cart.lock();
        cartsRepository.save(cart);
        return true;
    }

    @Override
    public boolean unlockCart(long cartId, String userId) {
        log.info(String.format("Cart unlock requested by userId %s for cartId %d", userId, cartId));

        Cart cart = cartsRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart not found: (cartId - %d)", cartId)));

        if (cart.isUnlocked()) {
            log.info(String.format("Cart already unlocked: (cartId - %d). Skipping..", cartId));
            return false;
        }
        cart.unlock();
        cartsRepository.save(cart);
        return true;
    }

    @Override
    public void initiateCheckout(long cartId, String userId) {
        boolean locked = this.lockCart(cartId, userId);
        if (locked) {
            //TODO: create pending order
        }
    }

    private void ensureProductExistsAndAlsoInStock(long productId) {
        Optional<Product> productOptional = this.productsRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException(String.format("Product not found: (productId - %d)", productId));
        }

        if (productOptional.filter(Product::isAvailable).isEmpty()) {
            throw new ProductOutOfStockException(productId);
        }
    }

    private void ensureCartExistsAndNotLocked(long cartId, String userId) {
        Optional<Cart> cartOptional = this.cartsRepository.findByIdAndUserId(cartId, userId);
        if (cartOptional.isEmpty()) {
            throw new CartNotFoundException(String.format("Cart not found: (cartId - %d) for user: (userId - %s)", cartId, userId));
        }

        if (cartOptional.filter(c -> !c.isLocked()).isEmpty()) {
            throw new CartLockedException(cartId);
        }
    }
}
