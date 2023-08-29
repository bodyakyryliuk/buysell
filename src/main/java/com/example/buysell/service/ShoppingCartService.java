package com.example.buysell.service;

import com.example.buysell.model.Product;
import com.example.buysell.model.User;
import java.util.List;

public interface ShoppingCartService {

    void addItem(User user, Product product);

    void removeItem(User user, Product product);

    List<Product> findByTitle(User user, String title);

    List<Product> findAllItems(User user);
}
