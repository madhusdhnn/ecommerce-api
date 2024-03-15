package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartsRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findById(long cartId);

    Optional<Cart> findByIdAndUserId(long cartId, String userId);

}
