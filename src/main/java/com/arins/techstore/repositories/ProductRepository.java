package com.arins.techstore.repositories;

import com.arins.techstore.entitys.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT t FROM Product t WHERE t.userId = :userId")
    List<Product> findProductByUserId(@Param("userId") String userId);

    List<Product> findByUserId(String userId);
}
