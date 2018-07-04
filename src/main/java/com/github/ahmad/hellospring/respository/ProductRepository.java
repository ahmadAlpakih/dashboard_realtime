package com.github.ahmad.hellospring.respository;

import com.github.ahmad.hellospring.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select count (title) from Product where title='computer'")
    Long findCount(String expr);

    //sudah terjual
    @Query(value = "select count (status) from Product where status=0")
    Integer findCountStatusTerjual(Integer expr);

    //sudah terjual
    @Query(value = "select count (status) from Product where status=1")
    Integer findCountStatusBelumTerjual(Integer expr);

    Optional<Product> findByName(String name);


}
