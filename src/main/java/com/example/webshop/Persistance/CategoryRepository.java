package com.example.webshop.Persistance;

import com.example.webshop.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Transactional
    Integer removeById(Long id);
}
