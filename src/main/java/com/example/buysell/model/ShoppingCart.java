package com.example.buysell.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Table(name = "shopping_cart")
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Integer> cartItems = new HashMap<>();

    public ShoppingCart(User user){
        this.user = user;
    }


    public void addProduct(Product product, int quantity){
        cartItems.put(product, quantity);
    }

    public void removeProduct(Product product){
        cartItems.remove(product);
    }



}
