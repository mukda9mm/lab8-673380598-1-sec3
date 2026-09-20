package com.example.demo.strategy;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public Double calculatePrice(Double price) {
        return price;
    }
}
