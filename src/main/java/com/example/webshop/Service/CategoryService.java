package com.example.webshop.Service;

import com.example.webshop.Model.Category;
import com.example.webshop.Model.Exception.ProductWithThisCategoryExists;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category update(Long id,Category category);
    Integer deleteById(Long id) throws ProductWithThisCategoryExists;
}
