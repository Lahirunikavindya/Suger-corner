package com.sugarcorner.service;

import com.sugarcorner.model.entity.Product;
import com.sugarcorner.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getFeaturedProducts(int limit) {
        List<Product> all = productRepository.findByActiveTrue();
        return all.stream().limit(limit).toList();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String search) {
        if (search == null || search.isBlank()) {
            return getAllActiveProducts();
        }
        return productRepository.searchProducts(search.trim());
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Product productDetails) {
        Product product = getById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImageUrl(productDetails.getImageUrl());
        product.setActive(productDetails.isActive());
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = getById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional
    public void reduceStock(Long productId, int quantity) {
        Product product = getById(productId);
        int newStock = product.getStockQuantity() - quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }
        product.setStockQuantity(newStock);
        productRepository.save(product);
    }
}
