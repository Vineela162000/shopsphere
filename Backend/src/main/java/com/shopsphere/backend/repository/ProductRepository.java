package com.shopsphere.backend.repository;

import com.shopsphere.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// Spring Data JPA: we write only the interface. Spring creates the real class when the app starts.
// Core Java: one interface can extend many interfaces.
// Generics: <Product, Long> = "works with Product, and its id is a Long".
public interface ProductRepository extends JpaRepository<Product, Long>,       // findAll, findById, save, delete
                                           JpaSpecificationExecutor<Product> { // NEW: findAll(filters, page)
}