package com.example.buysell.service.shoppingCart;

import com.example.buysell.model.Product;
import com.example.buysell.model.ShoppingCart;
import com.example.buysell.model.User;
import com.example.buysell.repository.ShoppingCartRepository;
import com.example.buysell.service.products.ProductService;
import com.example.buysell.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public void addItem(User user, Product product, int quantity) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        shoppingCart.addProduct(product, quantity);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void removeItem(User user, Product product) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        shoppingCart.removeProduct(product);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public Set<Product> findByTitle(User user, String title) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        if (!StringUtils.isEmpty(title))
            return shoppingCart.getCartItems().keySet().stream()
                    .filter(product -> product.getTitle().equals(title))
                    .collect(Collectors.toSet());
        else
            return shoppingCart.getCartItems().keySet();
    }

    @Override
    public Set<Product> findAllItems(User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();

        return shoppingCart.getCartItems().keySet();
    }

    @Override
    public void updateQuantity(User user, Product product, int quantity, String action) {
        if (action.equals("set"))
            setQuantity(user, product, quantity);
        else if (action.equals("increase"))
            increaseQuantity(user, product);
        else
            decreaseQuantity(user, product);
    }

    @Override
    public void decreaseQuantity(User user, Product product) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        shoppingCart.getCartItems().put(product, shoppingCart.getCartItems().get(product) - 1);

        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void increaseQuantity(User user, Product product) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        shoppingCart.getCartItems().put(product, shoppingCart.getCartItems().get(product) + 1);

        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void setQuantity(User user, Product product, int quantity) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        shoppingCart.getCartItems().put(product, quantity);

        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public Map<Long, Integer> getExistingQuantities(User user) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        Map<Long, Integer> existingQuantities = new HashMap<>();
        for (Product product: shoppingCart.getCartItems().keySet()){
            existingQuantities.put(product.getId(), shoppingCart.getCartItems().get(product));
        }
        return existingQuantities;
    }

    @Override
    public User getUser() {
        return userService.getLoggedInUser();
    }


    @Override
    public Product getProductById(Long id){
        return productService.getProductById(id);
    }


}















