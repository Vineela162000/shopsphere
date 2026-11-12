package com.shopsphere.backend.service;

import com.shopsphere.backend.dto.ProductFilter;
import com.shopsphere.backend.dto.ProductRequest;
import com.shopsphere.backend.dto.ProductResponse;
import com.shopsphere.backend.entity.Product;
import com.shopsphere.backend.exception.ProductNotFoundException;
import com.shopsphere.backend.repository.ProductRepository;
import com.shopsphere.backend.repository.ProductSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Spring Core: Spring creates one ProductService and shares it
public class ProductService {

    private final ProductRepository productRepository;

    // Spring Core (Dependency Injection): Spring hands us the repository
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ---------- Customer side (reading) ----------

    // Spring Data (Pageable + Page): one page of filtered, sorted sarees
    public Page<ProductResponse> searchProducts(ProductFilter filter, Pageable pageable) {
        return productRepository
                .findAll(ProductSpecifications.withFilter(filter), pageable)
                .map(ProductResponse::from);              // Java 8 (method reference)
    }

    public ProductResponse getProductById(Long id) {
        return ProductResponse.from(findOrThrow(id));
    }

    // ---------- Owner side (writing) ----------

    // Spring (Transactional): all database work here succeeds together or is undone together
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        copyFields(request, product);
        product.setActive(true);                          // system field: the owner doesn't set this
        return ProductResponse.from(productRepository.save(product)); // INSERT
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findOrThrow(id);
        copyFields(request, product);
        // Hibernate (dirty checking): no save() needed. Hibernate notices the changed
        // fields and runs the UPDATE by itself when the transaction ends.
        return ProductResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = findOrThrow(id);
        product.setActive(false);                         // soft delete: hidden from the shop, data kept
    }

    // ---------- Helpers ----------

    // Java 8 (Optional + lambda): found → return it, not found → throw our 404 error
    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    // Copies the owner's form into the database object (used by both add and edit)
    private void copyFields(ProductRequest request, Product product) {
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setWeave(request.weave());
        product.setOccasion(request.occasion());
        product.setColor(request.color());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImageUrl(request.imageUrl());
        product.setBestseller(Boolean.TRUE.equals(request.bestseller())); // null → false
    }
}