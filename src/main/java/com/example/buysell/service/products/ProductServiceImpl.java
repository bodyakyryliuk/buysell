package com.example.buysell.service.products;

import com.example.buysell.model.Product;
import com.example.buysell.model.User;
import com.example.buysell.repository.ProductRepository;
import com.example.buysell.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserServiceImpl userService;

    @Override
    public List<Product> getProductsByTitle(String title){
        if (!StringUtils.isEmpty(title))
            return productRepository.findByTitle(title);
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(Product product){
        log.info("Saving new {}", product);
        productRepository.save(product);
    }

    @Override
    public void removeProduct(Long id){
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void addProductsToModel(String title, Model model) {
        User currUser = userService.getLoggedInUser();

        model.addAttribute("allProducts", getProductsByTitle(title));
        model.addAttribute("user", currUser);
    }

    @Override
    public void addProductsToModel(Long id, Model model) {
        model.addAttribute(getProductById(id));
    }
}




