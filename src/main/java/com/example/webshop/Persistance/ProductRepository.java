package com.example.webshop.Persistance;

import com.example.webshop.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByCategoryId(Long id);
    boolean existsByCategoryId(Long id);
}
