package com.example.webshop.Web;

import com.example.webshop.Model.Product;
import com.example.webshop.Model.ShoppingCart;
import com.example.webshop.Model.dto.ChargeRequest;
import com.example.webshop.Service.AuthService;
import com.example.webshop.Service.ShoppingCartService;
import com.example.webshop.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Value("${STRIPE_P_KEY}")
    private String publicKey;

    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public PaymentController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping ("/charge")
    public String getChargePage(Model model) {
        try {
            ShoppingCart activeShoppingCartByUsername = this.shoppingCartService.findActiveShoppingCartByUsername(this.authService.getCurrentUserId());
            model.addAttribute("shoppingCart", activeShoppingCartByUsername);
            model.addAttribute("currency", "eur");
            model.addAttribute("amount", ((int) activeShoppingCartByUsername.getProducts().stream().mapToDouble(Product::getPrice).sum() * 100));
            model.addAttribute("stripePublicKey", this.publicKey);
            return "checkout";
        } catch (RuntimeException ex) {
            return "redirect:/products?error=error" + ex.getLocalizedMessage();
        }
    }

    @PostMapping("/charge")
    public String checkout(ChargeRequest chargeRequest, Model model) {
        try {
            this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
            return "redirect:/products?message=Successful Payment";
        } catch (RuntimeException ex) {
            return "redirect:/payments/charge?error=" + ex.getLocalizedMessage();
        }
    }

}
