package com.example.demo.strategy;

public class MemberDiscountStrategy implements DiscountStrategy{
    @Override
    public Double calculatePrice(Double price){
        return price*0.9;
    }
}
