package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.CartNotBelongsToUserException;
import com.thetechmaddy.ecommerce.exceptions.CartNotFoundException;
import com.thetechmaddy.ecommerce.exceptions.ProductNotInCartException;
import com.thetechmaddy.ecommerce.exceptions.ProductOutOfStockException;
import com.thetechmaddy.ecommerce.models.CheckoutData;
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
import static com.thetechmaddy.ecommerce.utils.CartUtils.verifyCartOwnerAndLockStatus;

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
            throw new CartNotBelongsToUserException(String.format("Cart:(cartId - %d) does not belong to the user - %s", cartId, userId));
        }

        calculateSubTotal(cart);
        return cart;
    }

    @Override
    @Transactional
    public void addProductToCart(long cartId, String userId, CartItemRequest cartItemRequest) {
        long productId = cartItemRequest.getProductId();

        Cart cart = getUserCart(cartId, userId);
        verifyCartOwnerAndLockStatus(userId, cart);

        Product product = productsService.checkQuantityAndGetProduct(productId, cartItemRequest.getQuantity());

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId() == productId)
                .findFirst();

        CartItem cartItem;
        if (cartItemOptional.isPresent()) {
            cartItem = cartItemOptional.get();
            cartItem.incrementQuantity(cartItemRequest.getQuantity());
        } else {
            cartItem = new CartItem(product, cartItemRequest.getQuantity(), SELECTED, cart);
        }

        cart.addProduct(cartItem);
        cartsRepository.save(cart);

        log.info(String.format("Product: (productId - %d) added/ updated in the cart: (cartId - %d) by the user: (userId - %s)",
                productId, cart.getId(), userId)
        );
    }

    @Override
    @Transactional
    public void updateProductInCart(long cartId, long productId, String userId, CartItemUpdateRequest cartItemUpdateRequest) {
        try {
            productsService.ensureProductInStock(productId);
            ensureCartExistsAndNotLocked(cartId, userId);

            CartItem cartItem = cartItemsRepository.findByCartIdAndProductId(cartId, productId)
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
            log.info(
                    String.format("Deleted product: (productId - %d) from cart: (cartId - %d) by user: (userId - %s)",
                            productId, cartId, userId
                    )
            );
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
        Cart cart = getUserCart(cartId, userId);

        log.info(String.format("Cart lock requested by userId %s for cartId %d", userId, cart.getId()));
        cartLockApplierService.acquireLock(cart, "-");

        log.info(String.format("Cart locked by userId %s for cartId %d", userId, cart.getId()));
        return cart.isLocked();
    }

    @Override
    public boolean unlockCart(long cartId, String userId) {
        Cart cart = getUserCart(cartId, userId);

        log.info(String.format("Cart unlock requested by userId %s for cartId %d", userId, cart.getId()));
        cartLockApplierService.releaseLock(cart, "-");

        log.info(String.format("Cart unlocked by userId %s for cartId %d", userId, cart.getId()));
        return cart.isUnlocked();
    }

    @Override
    public Cart getUserCart(String userId) {
        return cartsRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart for user: (userId - %s) not found", userId)));
    }

    @Override
    public CheckoutData checkoutCart(long cartId, String userId) {
        ensureCartExistsAndNotLocked(cartId, userId);

        BigDecimal grossTotal = cartItemsRepository.getTotal(cartId, userId);
        return new CheckoutData(cartId, grossTotal);
    }

    private void calculateSubTotal(Cart cart) {
        BigDecimal subTotal = cart.getCartItems()
                .stream()
                .filter(CartItem::isSelected)
                .map(ci -> ci.getProduct().getGrossAmount().multiply(new BigDecimal(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubTotal(subTotal);
    }

    private Cart getUserCart(long cartId, String userId) {
        return cartsRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new CartNotFoundException(String.format("Cart: (cartId - %d) not found", cartId)));
    }

    private void ensureCartExistsAndNotLocked(long cartId, String userId) {
        Cart cart = getUserCart(cartId, userId);
        verifyCartOwnerAndLockStatus(userId, cart);
    }
}
