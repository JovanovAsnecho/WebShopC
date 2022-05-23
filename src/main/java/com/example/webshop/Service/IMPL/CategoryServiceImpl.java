package com.example.webshop.Service.IMPL;

import com.example.webshop.Model.Category;
import com.example.webshop.Model.Exception.CategoryNotFoundException;
import com.example.webshop.Model.Exception.ProductWithThisCategoryExists;
import com.example.webshop.Persistance.CategoryRepository;
import com.example.webshop.Persistance.ProductRepository;
import com.example.webshop.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(()-> new CategoryNotFoundException(id));
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category updatedCategory = this.findById(id);
        updatedCategory.setName(category.getName());
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public Integer deleteById(Long id) {
        Category category = this.findById(id);
        if(productRepository.existsByCategoryId(id)){
            try {
                throw new ProductWithThisCategoryExists(id);
            } catch (ProductWithThisCategoryExists productWithThisCategoryExists) {
                productWithThisCategoryExists.printStackTrace();
            }
        }
        return this.categoryRepository.removeById(id);
    }
}
