package com.example.buysell.controller;

import com.example.buysell.model.Product;
import com.example.buysell.model.User;
import com.example.buysell.service.ProductService;
import com.example.buysell.service.ShoppingCartServiceImpl;
import com.example.buysell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ProductService productService;
    private final UserService userService;
    private final ShoppingCartServiceImpl shoppingCartService;

    @GetMapping
    public String getShoppingCart(@RequestParam(name = "title", required = false) String title, Model model){
        User user = userService.getLoggedInUser();
        model.addAttribute("cartItems", shoppingCartService.findByTitle(user, title));
        model.addAttribute("user", user);
        return "shopping-cart";
    }

    @GetMapping("/add-product/{id}")
    public String addProductToCart(@PathVariable Long id){
        Product product = productService.getProductById(id);
        User user = userService.getLoggedInUser();

        shoppingCartService.addItem(user, product);
        return "redirect:/shopping-cart";
    }

    @GetMapping("/remove-product/{id}")
    public String removeProductFromCart(@PathVariable Long id){
        Product product = productService.getProductById(id);
        User user = userService.getLoggedInUser();

        shoppingCartService.removeItem(user, product);
        return "redirect:/shopping-cart";
    }

}




