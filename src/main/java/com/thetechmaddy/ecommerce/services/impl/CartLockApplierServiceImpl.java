package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.services.CartLockApplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
        }
    }

    public void releaseLock(Cart cart) {
        ensureCartIsNotNull(cart);
        if (cart.isLocked()) {
            cart.unlock();
            cartsRepository.save(cart);
        }
    }

    private static void ensureCartIsNotNull(Cart cart) {
        if (cart == null) {
            throw new NullPointerException("cart == null");
        }
    }
}
