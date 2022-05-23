package com.example.webshop.Model.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ProductWithThisCategoryExists extends Throwable {
    public ProductWithThisCategoryExists(Long id) {
        super(String.format("Product with category %d exists, id"));
    }
}
