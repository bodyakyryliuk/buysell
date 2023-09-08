package com.example.buysell.controller;

import com.example.buysell.service.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping("/{id}")
    public String purchaseProduct(@PathVariable Long id){
        if (purchaseService.purchaseProductById(id))
            return "purchase-success";
        else
            return "purchase-failure";
    }

}
