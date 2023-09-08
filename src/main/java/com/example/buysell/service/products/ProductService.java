package com.example.buysell.service.products;

import com.example.buysell.model.Product;
import org.springframework.ui.Model;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product);

    void removeProduct(Long id);

    List<Product> getProductsByTitle(String title);

    Product getProductById(Long id);

    void addProductsToModel(String title, Model model);

    void addProductsToModel(Long id, Model model);
}
