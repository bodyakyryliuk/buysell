package com.example.buysell.service;

import com.example.buysell.model.Product;
import com.example.buysell.model.ShoppingCart;
import com.example.buysell.model.User;
import com.example.buysell.repository.ShoppingCartRepository;
import com.example.buysell.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public void addItem(User user, Product product) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        shoppingCart.addProduct(product);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void removeItem(User user, Product product) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        shoppingCart.removeProduct(product);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public List<Product> findByTitle(User user, String title) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        if (!StringUtils.isEmpty(title))
            return shoppingCart.getProducts().stream()
                    .filter(product -> product.getTitle().equals(title))
                    .collect(Collectors.toList());
        else
            return shoppingCart.getProducts();
    }

    @Override
    public List<Product> findAllItems(User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        return shoppingCart.getProducts();
    }
}
