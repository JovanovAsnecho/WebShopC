package com.example.webshop.Web.Rest;

import com.example.webshop.Model.Category;
import com.example.webshop.Model.Exception.ProductWithThisCategoryExists;
import com.example.webshop.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> findAll() {
        return this.categoryService.findAll();
    }
    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        return this.categoryService.findById(id);
    }
    @PostMapping()
    public Category save(Category category) {
        return this.categoryService.save(category);
    }
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id,Category category) {
        return this.categoryService.update(id, category);
    }
    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable Long id) throws ProductWithThisCategoryExists {
        return this.categoryService.deleteById(id);
    }
}
