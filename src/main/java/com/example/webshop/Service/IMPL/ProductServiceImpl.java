package com.example.webshop.Service.IMPL;

import com.example.webshop.Model.Category;
import com.example.webshop.Model.Exception.ProductNotFoundException;
import com.example.webshop.Model.Product;
import com.example.webshop.Persistance.ProductRepository;
import com.example.webshop.Service.CategoryService;
import com.example.webshop.Service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product save(Product product) throws IOException {
        Category category =this.categoryService.findById(product.getCategory().getId());
        product.setCategory(category);
        return this.productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product, MultipartFile image) throws IOException {
        Product updatedProduct = this.findById(id);
        Category category = this.categoryService.findById(product.getCategory().getId());

        updatedProduct.setCategory(category);
        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setQuantity(product.getQuantity());
        updatedProduct.setDescription(product.getDescription());
        return this.productRepository.save(updatedProduct);
    }

    @Override
    public void delete(Long id) {
    this.productRepository.deleteById(id);
    }

    @Override
    public boolean existsByCategoryId(Long id) {
        return this.productRepository.existsByCategoryId(id);
    }
}
