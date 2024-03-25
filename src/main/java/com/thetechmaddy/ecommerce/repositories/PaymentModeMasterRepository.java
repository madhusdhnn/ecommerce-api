package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.payments.PaymentModeMaster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentModeMasterRepository extends CrudRepository<PaymentModeMaster, Long> {
    List<PaymentModeMaster> findAllBySupportedTrue();
}
