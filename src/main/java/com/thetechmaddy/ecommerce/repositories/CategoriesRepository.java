package com.thetechmaddy.ecommerce.repositories;

import com.thetechmaddy.ecommerce.domains.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {

    Category findByNameIgnoreCase(String name);

}
