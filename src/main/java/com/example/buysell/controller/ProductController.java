package com.example.buysell.controller;

import com.example.buysell.model.Product;
import com.example.buysell.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String showProductPage(@RequestParam(name = "title", required = false) String title, Model model){
        productService.addProductsToModel(title, model);
        return "products";
    }

    @PostMapping("/product/create")
    public String createProduct(Product product){
        productService.saveProduct(product);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.removeProduct(id);
        return "redirect:/";
    }

    @GetMapping("/product/{id}")
    public String getProductInfo(@PathVariable Long id, Model model){
        productService.addProductsToModel(id, model);
        return "product-info";
    }



}
