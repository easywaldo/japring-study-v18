package com.example.demo.product.service;

import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import com.example.demo.product.application.command.ProductRequestCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product addProduct(ProductRequestCommand.command command) {
        Product product = new Product(command.name(), command.price());
        productRepository.save(product);
        return product;
    }

    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return product;
    }
}
