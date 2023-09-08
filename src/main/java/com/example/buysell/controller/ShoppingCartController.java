package com.example.buysell.controller;

import com.example.buysell.model.User;
import com.example.buysell.service.shoppingCart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public String getShoppingCart(@RequestParam(name = "title", required = false) String title, Model model){
        User user = shoppingCartService.getUser();
        model.addAttribute("cartItems", shoppingCartService.findByTitle(user, title));
        model.addAttribute("user", user);
        model.addAttribute("existingQuantities", shoppingCartService.getExistingQuantities(user));
        return "shopping-cart";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToCart(@PathVariable Long id, @RequestParam(name = "quantity") int quantity){
        shoppingCartService.addItem(shoppingCartService.getUser(),
                                    shoppingCartService.getProductById(id),
                                    quantity);

        return "redirect:/shopping-cart";
    }

    @PostMapping("/remove-product/{id}")
    public String removeProductFromCart(@PathVariable Long id){
        shoppingCartService.removeItem(shoppingCartService.getUser(),
                                       shoppingCartService.getProductById(id));

        return "redirect:/shopping-cart";
    }

    @PostMapping("/update-quantity/{id}")
    public String updateProductQuantity(@PathVariable Long id,
                                        @RequestParam(name = "action", required = false) String action,
                                        @RequestParam(name = "quantity", required = false) Integer quantity){

        shoppingCartService.updateQuantity(
                shoppingCartService.getUser(),
                shoppingCartService.getProductById(id),
                quantity,
                action
        );

        return "redirect:/shopping-cart";
    }
}




