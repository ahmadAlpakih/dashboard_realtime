package com.github.ahmad.hellospring.service.implementation;

import com.github.ahmad.hellospring.domain.Product;
import com.github.ahmad.hellospring.respository.ProductRepository;
import com.github.ahmad.hellospring.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductServices {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findOne(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Data tidak ditemukan !"));
    }

    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public boolean hasExist(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public Long findCountV2(String expr) {
        return productRepository.findCount(expr);
    }

    @Override
    public Integer findCountStatusTerjualV2(Integer expr) {
        return productRepository.findCountStatusBelumTerjual(expr);
    }

    @Override
    public Integer findCountStatusBelumTerjualV2(Integer expr) {
        return productRepository.findCountStatusTerjual(expr);
    }

    @Override
    public Long findCount() {
        return productRepository.count();
    }


    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }


    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
