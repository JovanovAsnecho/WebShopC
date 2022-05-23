package com.example.webshop.Web;

import com.example.webshop.Model.User;
import com.example.webshop.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class SecurityController {
    private UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(User user, BindingResult bindingResult, Model model) {
        boolean b = this.userService.userExists(user.getUsername());
        if (b) {
            return "redirect:/register?error=Username already taken";
        }
        this.userService.save(user);
        return "redirect:/login";
    }
}
