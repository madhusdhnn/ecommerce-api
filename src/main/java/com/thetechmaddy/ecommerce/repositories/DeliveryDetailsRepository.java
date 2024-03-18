package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetails, Long> {
}
