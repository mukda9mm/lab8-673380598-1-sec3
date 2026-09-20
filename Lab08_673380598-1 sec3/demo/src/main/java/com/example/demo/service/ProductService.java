package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;

@Service
public class ProductService {
    private final ProductRepository reProductRepository;
    private final ProductDetailRepository reDetailRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(ProductRepository reProductRepository, ProductDetailRepository reDetailRepository,
            ReviewRepository reviewRepository) {
        this.reProductRepository = reProductRepository;
        this.reDetailRepository = reDetailRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Product> showAllProduct() {
        return reProductRepository.findAll();
    }

    public Product showSomeProduct(Long id) {
        return reProductRepository.findById(id).orElse(null);
    }

    public void saveProduct(Product product) {
        for (Review review : product.getReviews()) {
            review.setProduct(product);
        }
        reProductRepository.save(product);
    }

    public void updateProduct(Long id,Product product){
        Product old_Product = showSomeProduct(id);
        product.setId(id);
        product.getDetail().setId(old_Product.getDetail().getId());
        product.setReviews(old_Product.getReviews());
        reProductRepository.save(product);
    }

    public void deleteProduct(Long id){
        reProductRepository.deleteById(id);
    }
}
