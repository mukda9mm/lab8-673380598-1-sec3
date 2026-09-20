package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProductController {
    ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public String showHomePage(Model model) {
        model.addAttribute("products", service.showAllProduct());
        return "products/list";
    }

    @GetMapping("/products/add")
    public String showAddPage(Model model) {
        Product product = new Product();
        product.getReviews().add(new Review());
        model.addAttribute("product", product);
        return "products/add";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product) {
        service.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.showSomeProduct(id));
        return "/products/edit";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id, Product product) {
        service.updateProduct(id, product);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String showDeletePage(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.showSomeProduct(id));
        return "/products/delete";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/products";
    }

}
