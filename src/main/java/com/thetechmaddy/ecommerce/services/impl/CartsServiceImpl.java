package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.requests.CartItemRequest;
import com.thetechmaddy.ecommerce.models.requests.CartItemUpdateRequest;
import com.thetechmaddy.ecommerce.repositories.CartItemsRepository;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.services.CartLockApplierService;
import com.thetechmaddy.ecommerce.services.CartsService;
import com.thetechmaddy.ecommerce.services.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.CartItemStatus.SELECTED;
import static com.thetechmaddy.ecommerce.models.CartItemStatus.UN_SELECTED;

@Log4j2
@Primary
@Service("cartsServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartsServiceImpl implements CartsService {

    private final CartsRepository cartsRepository;
    private final ProductsService productsService;
    private final CartItemsRepository cartItemsRepository;

    private final CartLockApplierService cartLockApplierService;

    @Override
    public Cart getCart(long cartId, String userId) {
        Cart cart = cartsRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart not found with id %d", cartId)));

        if (!cart.belongsTo(userId)) {
            throw new CartNotBelongsToUserException(String.format("Cart with id %d does not belong to the user - %s", cartId, userId));
        }

        calculateSubTotal(cart);
        return cart;
    }

    @Override
    @Transactional
    public void addProductToCart(long cartId, String userId, CartItemRequest cartItemRequest) {
        long productId = cartItemRequest.getProductId();

        productsService.ensureProductInStock(productId);

        ensureCartExistsAndNotLocked(cartId, userId);

        int rowsAffected = cartItemsRepository.saveOnConflictUpdateQuantity(productId, cartItemRequest.getQuantity(), cartId);
        log.info(String.format("Product: (productId - %d) added/ updated in the cart: (cartId - %d) by the user: (userId - %s). Affected rows: %d", productId, cartId, userId, rowsAffected));
    }

    @Override
    @Transactional
    public void updateProductInCart(long cartId, long productId, String userId, CartItemUpdateRequest cartItemUpdateRequest) {
        try {
            productsService.ensureProductInStock(productId);
            ensureCartExistsAndNotLocked(cartId, userId);

            CartItem cartItem = cartItemsRepository.findByCartIdAndProductId(cartId, productId) // TODO: Also fetch by attribute
                    .orElseThrow(() -> new ProductNotInCartException(productId));

            Integer quantity = cartItemUpdateRequest.getQuantity();
            if (quantity != null) {
                cartItem.setQuantity(quantity);
            }

            Boolean isSelected = cartItemUpdateRequest.getIsSelected();
            if (isSelected != null) {
                cartItem.setStatus(isSelected ? SELECTED : UN_SELECTED);
            }

            cartItemsRepository.save(cartItem);
        } catch (ProductOutOfStockException ex) {
            boolean removed = removeProductFromCart(cartId, productId, userId);
            if (removed) {
                log.info(String.format("Product: (productId - %d) removed from cart as it went out of stock", productId));
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public boolean removeProductFromCart(long cartId, long productId, String userId) {
        ensureCartExistsAndNotLocked(cartId, userId);

        int rowsDeleted = cartItemsRepository.removeItem(cartId, productId, userId);
        if (rowsDeleted == 1) {
            log.info(String.format("Deleted product: (productId - %d) from cart: (cartId - %d) by user: (userId - %s)", productId, cartId, userId));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean clearCart(long cartId, String userId) {
        ensureCartExistsAndNotLocked(cartId, userId);

        int rowsDeleted = cartItemsRepository.clearCartItems(cartId, userId);
        if (rowsDeleted > 0) {
            log.info(String.format("Cart: (cartId - %d) cleared by user: (userId - %s).", cartId, userId));
            return true;
        }
        return false;
    }

    @Override
    public boolean lockCart(long cartId, String userId) {
        log.info(String.format("Cart lock requested by userId %s for cartId %d", userId, cartId));

        try {
            Cart cart = cartsRepository.findByIdAndUserId(cartId, userId)
                    .orElseThrow(() -> new CartNotFoundException(String.format("Cart not found: (cartId - %d)", cartId)));

            cartLockApplierService.acquireLock(cart);

            log.info(String.format("Cart locked by userId %s for cartId %d", userId, cartId));
            return true;
        } catch (Exception ex) {
            log.error(String.format("Cart lock failed: (cartId - %d)..", cartId), ex);
            return false;
        }
    }

    @Override
    public boolean unlockCart(long cartId, String userId) {
        log.info(String.format("Cart unlock requested by userId %s for cartId %d", userId, cartId));

        try {
            Cart cart = cartsRepository.findByIdAndUserId(cartId, userId)
                    .orElseThrow(() -> new CartNotFoundException(String.format("Cart not found: (cartId - %d)", cartId)));

            cartLockApplierService.releaseLock(cart);

            log.info(String.format("Cart unlocked by userId %s for cartId %d", userId, cartId));
            return true;
        } catch (Exception ex) {
            log.info(String.format("Cart unlocked failed: (cartId - %d)..", cartId), ex);
            return false;
        }
    }

    private void calculateSubTotal(Cart cart) {
        BigDecimal subTotal = cart.getCartItems()
                .stream()
                .filter(CartItem::isSelected)
                .map(ci -> ci.getProduct().getGrossAmount().multiply(new BigDecimal(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubTotal(subTotal);
    }

    private void ensureCartExistsAndNotLocked(long cartId, String userId) {
        Optional<Cart> cartOptional = cartsRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            throw new CartNotFoundException(String.format("Cart not found: (cartId - %d) for user: (userId - %s)", cartId, userId));
        }

        if (cartOptional.filter(c -> c.belongsTo(userId)).isEmpty()) {
            throw new CartNotBelongsToUserException(String.format("Cart: (cartId - %d) does not belong to the user: (userId - %s)", cartId, userId));
        }

        if (cartOptional.filter(c -> !c.isLocked()).isEmpty()) {
            throw new CartLockedException(cartId);
        }
    }
}
