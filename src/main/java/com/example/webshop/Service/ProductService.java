package com.example.webshop.Service;

import com.example.webshop.Model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product) throws IOException;
    Product update(Long id, Product product, MultipartFile image) throws IOException;
    void delete(Long id);
    boolean existsByCategoryId(Long id);
}
