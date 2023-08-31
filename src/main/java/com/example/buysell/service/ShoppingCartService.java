package com.example.buysell.service;

import com.example.buysell.model.Product;
import com.example.buysell.model.User;

import java.util.Map;
import java.util.Set;

public interface ShoppingCartService {

    void addItem(User user, Product product, int quantity);

    void removeItem(User user, Product product);

    Set<Product> findByTitle(User user, String title);

    Set<Product> findAllItems(User user);

    void decreaseQuantity(User user, Product product);
    void increaseQuantity(User user, Product product);
    void updateQuantity(User user, Product product, int quantity);

    Map<Long, Integer> getExistingQuantities(User user);
}
