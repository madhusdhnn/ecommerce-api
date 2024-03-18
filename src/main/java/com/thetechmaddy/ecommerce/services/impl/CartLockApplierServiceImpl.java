package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.services.CartLockApplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Log4j2
@Primary
@Service("cartLockApplierServiceImpl")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartLockApplierServiceImpl implements CartLockApplierService {

    private final CartsRepository cartsRepository;

    public void acquireLock(Cart cart) {
        ensureCartIsNotNull(cart);
        if (cart.isUnlocked()) {
            cart.lock();
            cartsRepository.save(cart);
            log.info(String.format("Lock acquired for cart: (cartId - %d) at %s", cart.getId(), OffsetDateTime.now()));
        }
    }

    public void releaseLock(Cart cart) {
        ensureCartIsNotNull(cart);
        if (cart.isLocked()) {
            cart.unlock();
            cartsRepository.save(cart);
            log.info(String.format("Lock released for cart: (cartId - %d) at %s", cart.getId(), OffsetDateTime.now()));
        }
    }

    private static void ensureCartIsNotNull(Cart cart) {
        if (cart == null) {
            throw new NullPointerException("cart == null");
        }
    }
}
