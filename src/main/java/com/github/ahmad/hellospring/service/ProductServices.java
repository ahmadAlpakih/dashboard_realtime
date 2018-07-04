package com.github.ahmad.hellospring.service;

import com.github.ahmad.hellospring.domain.Product;

import java.util.Optional;

public interface ProductServices {

    Product saveProduct(Product product);

    Product findOne(Long id);

    Iterable<Product> findAll();

    boolean hasExist(Long id);

    Long findCountV2(String expr);

    Integer findCountStatusTerjualV2(Integer expr);

    Integer findCountStatusBelumTerjualV2(Integer expr);

    Long findCount();

    Optional<Product> findByName(String name);

    void deleteProduct(Long id);


}
