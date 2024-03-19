package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.carts.Cart;

public interface CartLockApplierService {

    void acquireLock(Cart cart, String reason);

    void releaseLock(Cart cart, String reason);

}
