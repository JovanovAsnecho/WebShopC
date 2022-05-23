package com.example.webshop.Model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CartItem {


    @Id
    private Long id;

    @ManyToOne
    private ShoppingCart shopingCart;

    @ManyToOne
    private Product product;

    private Integer quatity;
}
