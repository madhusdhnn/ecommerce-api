package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.users.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressesRepository extends JpaRepository<UserAddress, Long> {

    @Query(value = "SELECT ua FROM UserAddress ua WHERE ua.user.cognitoSub = :userId")
    List<UserAddress> findAllByUserId(@Param("userId") String userId);

}
