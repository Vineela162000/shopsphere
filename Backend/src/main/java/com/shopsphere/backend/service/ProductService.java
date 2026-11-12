package com.shopsphere.backend.service;

import com.shopsphere.backend.dto.ProductFilter;
import com.shopsphere.backend.dto.ProductResponse;
import com.shopsphere.backend.exception.ProductNotFoundException;
import com.shopsphere.backend.repository.ProductRepository;
import com.shopsphere.backend.repository.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service // Spring Core: Spring creates one ProductService and shares it
public class ProductService {

    private final ProductRepository productRepository;

    // Spring Core (Dependency Injection): Spring hands us the repository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Spring Data (Pageable): "which page, how many per page, sorted how"
    // Spring Data (Page): one page of results + total count + total pages
    public Page<ProductResponse> searchProducts(ProductFilter filter, Pageable pageable) {
        return productRepository
                .findAll(ProductSpecifications.withFilter(filter), pageable) // SQL with WHERE + LIMIT + ORDER BY
                .map(ProductResponse::from);                                  // Java 8 (method reference): Product → ProductResponse
    }

    // Java 8 (Optional + lambda): found → convert it, not found → throw our 404 error
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}