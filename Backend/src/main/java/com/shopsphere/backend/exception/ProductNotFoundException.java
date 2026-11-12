package com.shopsphere.backend.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Saree not found with id " + id);
    }
}