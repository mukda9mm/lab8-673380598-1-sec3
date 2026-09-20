package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.strategy.DiscountContext;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String brand;
    private int stock;
    private Double price;
    private String discountType;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "detail_id", referencedColumnName = "id")
    private ProductDetail detail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Constructor
    public Product() {
        this.detail = new ProductDetail();
    }

    public Product(String name, String category, String brand, int stock, Double price, String discountType,
            ProductDetail detail) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.stock = stock;
        this.price = price;
        this.discountType = discountType;
        this.detail = detail;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public int getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getDiscountType() {
        return discountType;
    }

    public ProductDetail getDetail() {
        return detail;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Double getDiscountedPrice() {
        DiscountContext context = new DiscountContext();
        return context.calDiscount(price, discountType);
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setDetail(ProductDetail detail) {
        this.detail = detail;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
