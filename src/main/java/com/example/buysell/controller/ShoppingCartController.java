package com.example.buysell.controller;

import com.example.buysell.model.Product;
import com.example.buysell.model.User;
import com.example.buysell.service.ProductService;
import com.example.buysell.service.ShoppingCartServiceImpl;
import com.example.buysell.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController {
    private final ProductService productService;
    private final UserService userService;
    private final ShoppingCartServiceImpl shoppingCartService;

    @GetMapping
    public String getShoppingCart(@RequestParam(name = "title", required = false) String title, Model model){
        User user = userService.getLoggedInUser();
        Map<Long, Integer> existingQuantities = shoppingCartService.getExistingQuantities(user);
        model.addAttribute("cartItems", shoppingCartService.findByTitle(user, title));
        model.addAttribute("user", user);
        model.addAttribute("existingQuantities", existingQuantities);
        return "shopping-cart";
    }

    @PostMapping("/add-product/{id}")
    public String addProductToCart(@PathVariable Long id, @RequestParam(name = "quantity") int quantity){
        Product product = productService.getProductById(id);
        User user = userService.getLoggedInUser();

        shoppingCartService.addItem(user, product, quantity);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/remove-product/{id}")
    public String removeProductFromCart(@PathVariable Long id){
        Product product = productService.getProductById(id);
        User user = userService.getLoggedInUser();

        shoppingCartService.removeItem(user, product);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/update-quantity/{id}")
    public String updateProductQuantity(@PathVariable Long id,
                                        @RequestParam(name = "action", required = false) String action,
                                        @RequestParam(name = "quantity", required = false) Integer quantity){
        Product product = productService.getProductById(id);
        User user = userService.getLoggedInUser();
        log.warn(String.valueOf(product.getId()));
        log.warn(String.valueOf(quantity));
        log.warn(action);

        if (action.equals("set"))
            shoppingCartService.updateQuantity(user, product, quantity);
        else if (action.equals("increase"))
            shoppingCartService.increaseQuantity(user, product);
        else
            shoppingCartService.decreaseQuantity(user, product);

        return "redirect:/shopping-cart";
    }
}




