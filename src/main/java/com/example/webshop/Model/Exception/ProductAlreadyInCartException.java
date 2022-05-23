package com.example.webshop.Model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ProductAlreadyInCartException extends RuntimeException {
    public ProductAlreadyInCartException(Long productId) {
        super(String.format("Product with id %d already in cart",productId));
    }
}
