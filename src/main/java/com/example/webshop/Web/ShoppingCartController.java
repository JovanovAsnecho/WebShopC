package com.example.webshop.Web;

import com.example.webshop.Service.AuthService;
import com.example.webshop.Service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart/product")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable(name = "id") Long id) {
        try {

            this.shoppingCartService.addBookToShoppingCart(this.authService.getCurrentUserId(), id);
            return "redirect:/products";
        } catch (RuntimeException ex) {
            return "redirect:/products?error=" + ex.getLocalizedMessage();
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteProductToCart(@PathVariable(name = "id") Long id) {
        this.shoppingCartService.removeProductFromShoppingCart(this.authService.getCurrentUserId(), id);
        return "redirect:/products";
    }
}
