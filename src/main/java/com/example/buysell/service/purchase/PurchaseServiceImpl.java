package com.example.buysell.service.purchase;

import com.example.buysell.service.products.ProductService;
import com.example.buysell.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final UserServiceImpl userService;
    private final ProductService productService;

    @Override
    public boolean purchaseProductById(Long id) {
        if (userService.getLoggedInUser().getBalance().compareTo(productService.getProductById(id).getPrice()) >= 0) {
            userService.withdrawMoney(productService.getProductById(id).getPrice());
            return true;
        }
        else
            return false;
    }
}
