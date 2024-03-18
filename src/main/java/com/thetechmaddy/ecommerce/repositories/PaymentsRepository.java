package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.payments.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {

}
