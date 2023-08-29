package com.example.buysell.controller;

import com.example.buysell.service.ProductService;
import com.example.buysell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/{id}")
    public String purchaseProduct(@PathVariable Long id){
        if (userService.getLoggedInUser().getBalance().compareTo(productService.getProductById(id).getPrice()) >= 0) {
            userService.withdrawMoney(productService.getProductById(id).getPrice());
            return "purchase-success";
        }
        else
            return "purchase-failure";
    }



}
