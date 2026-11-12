package com.shopsphere.backend.controller;

import com.shopsphere.backend.dto.ProductFilter;
import com.shopsphere.backend.dto.ProductRequest;
import com.shopsphere.backend.dto.ProductResponse;
import com.shopsphere.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController                    // Spring MVC (Web): returns JSON, not a web page
@RequestMapping("/api/products")   // Spring MVC (Web): base URL for this class
public class ProductController {

    private final ProductService productService;

    // Spring Core (Dependency Injection)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ---------- Customer side ----------

    // GET /api/products?weave=..&maxPrice=..&page=0&size=12&sort=price,asc
    @GetMapping
    public PagedModel<ProductResponse> getProducts(
            @ModelAttribute ProductFilter filter,                   // Spring MVC (data binding): URL → record
            @PageableDefault(size = 12, sort = "id") Pageable pageable) {
        return new PagedModel<>(productService.searchProducts(filter, pageable));
    }

    // GET /api/products/5
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // ---------- Owner side (JWT login will lock these later) ----------

    // POST /api/products  → add a saree
    @PostMapping                                                    // Spring MVC (Web): POST = create
    @ResponseStatus(HttpStatus.CREATED)                             // REST: 201 Created
    public ProductResponse createProduct(
            @Valid                                                  // Bean Validation: check the rules first
            @RequestBody ProductRequest request) {                  // Spring MVC + Jackson: JSON → record
        return productService.createProduct(request);
    }

    // PUT /api/products/5  → edit saree 5
    @PutMapping("/{id}")                                            // Spring MVC (Web): PUT = replace/update
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @Valid @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // DELETE /api/products/5  → hide saree 5 (soft delete)
    @DeleteMapping("/{id}")                                         // Spring MVC (Web): DELETE = remove
    @ResponseStatus(HttpStatus.NO_CONTENT)                          // REST: 204 = done, nothing to send back
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}