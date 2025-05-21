package com.villainscode.entity;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;

    public Product(Long id, String name, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
