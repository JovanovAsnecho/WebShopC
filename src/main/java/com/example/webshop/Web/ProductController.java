package com.example.webshop.Web;


import com.example.webshop.Model.Category;
import com.example.webshop.Model.Product;
import com.example.webshop.Persistance.AuthorRepository;
import com.example.webshop.Service.CategoryService;
import com.example.webshop.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private AuthorRepository authorRepository;

    public ProductController(ProductService productService , CategoryService categoryService,AuthorRepository authorRepository){
        this.productService= productService;
        this.categoryService=categoryService;
        this.authorRepository=authorRepository;
    }

    @GetMapping
    public String ListProductsPage(Model model){
        List<Product> products = this.productService.findAll();
        model.addAttribute("products" , products);
        return  "products";
    }

    @PostMapping
    public String saveOrUpdateProduct(Model model,Product product, BindingResult bindingResult,
                                      @RequestParam MultipartFile image) throws IOException {
        if(bindingResult.hasErrors()) {
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            return "add-product";
        }

        try {
            if(product.getId() == null) {
                this.productService.save(product);
            }
            else{
                this.productService.update(product.getId(),product,image);
            }
        } catch (RuntimeException ex) {
            return "redirect:/product/add-new?error=" + "Problem";
        }
        return "redirect:/product";
    }

    @GetMapping("/add-new")
    public String addNewProductPage(Model model) {
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("product", new Product());
        model.addAttribute("author", this.authorRepository.findAll());
        model.addAttribute("categories", categories);
        return "add-product";
    }

    @GetMapping("/{id}/edit")
    public String editProductPage(@PathVariable Long id, Model model) {
        try {
            Product product = this.productService.findById(id);
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("product", product);
            model.addAttribute("authors", this.authorRepository.findAll());
            return "add-product";
        }catch (RuntimeException ex) {
            return "redirect:/product?error=" + ex.getLocalizedMessage();
        }

    }
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        this.productService.delete(id);
        return "redirect:/products";
    }

}
