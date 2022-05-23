package com.example.webshop.Service;

import com.example.webshop.Model.Product;
import com.example.webshop.Model.ShoppingCart;
import com.example.webshop.Model.dto.ChargeRequest;

import java.util.List;

public interface ShoppingCartService {

    List<Product> listItems(String username);

    ShoppingCart createShoppingCart(String username);

    ShoppingCart addBookToShoppingCart(String username, Long bookId);

    ShoppingCart removeProductFromShoppingCart(String username, Long bookId);

    ShoppingCart cancelShoppingCart(String userId);

    ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest);

    ShoppingCart findActiveShoppingCartByUsername(String userId);
}
