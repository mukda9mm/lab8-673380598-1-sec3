package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_Detail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    private String warranty;
    private Double weight;
    private String dimensions;
    private String manufacturedCountry;

    @OneToOne(mappedBy = "detail")
    private Product product;

    // Constructor
    public ProductDetail() {

    }

    public ProductDetail(Long id, String description, String warranty, Double weight, String dimensions,
            String manufacturedCountry, Product product) {
        this.id = id;
        this.description = description;
        this.warranty = warranty;
        this.weight = weight;
        this.dimensions = dimensions;
        this.manufacturedCountry = manufacturedCountry;
        this.product = product;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getWarranty() {
        return warranty;
    }

    public Double getWeight() {
        return weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getManufacturedCountry() {
        return manufacturedCountry;
    }

    public Product getProduct() {
        return product;
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public void setManufacturedCountry(String manufacturedCountry) {
        this.manufacturedCountry = manufacturedCountry;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
