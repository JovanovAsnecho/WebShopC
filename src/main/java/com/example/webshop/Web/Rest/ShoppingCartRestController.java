package com.example.webshop.Web.Rest;

import com.example.webshop.Model.Product;
import com.example.webshop.Model.ShoppingCart;
import com.example.webshop.Service.AuthService;
import com.example.webshop.Service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartRestController {
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public List<Product> listItems() {
        return shoppingCartService.listItems(this.authService.getCurrentUserId());
    }

    @PostMapping
    public ShoppingCart createShoppingCart() {
        return this.shoppingCartService.createShoppingCart(this.authService.getCurrentUserId());
    }

    @PatchMapping("/{productId}/products")
    public ShoppingCart addProductToShoppingCart(@PathVariable Long bookId) {
        return this.shoppingCartService.addBookToShoppingCart(this.authService.getCurrentUserId(), bookId);
    }

    @DeleteMapping("/{productId}/boooks")
    public ShoppingCart removeProductFromShoppingCart(@PathVariable Long bookId) {
        return this.shoppingCartService.removeProductFromShoppingCart(this.authService.getCurrentUserId(), bookId);
    }

    @DeleteMapping
    public ShoppingCart cancelShoppingCart() {
        return this.shoppingCartService.cancelShoppingCart(this.authService.getCurrentUserId());
    }

    @PostMapping("/checkout")
    public ShoppingCart checkoutShoppingCart() {
        return null;
    }


}
