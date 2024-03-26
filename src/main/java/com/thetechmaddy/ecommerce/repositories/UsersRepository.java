package com.thetechmaddy.ecommerce.repositories;


import com.thetechmaddy.ecommerce.domains.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}
