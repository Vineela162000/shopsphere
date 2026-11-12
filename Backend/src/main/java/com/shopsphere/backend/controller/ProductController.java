package com.shopsphere.backend.controller;

import com.shopsphere.backend.dto.ProductFilter;
import com.shopsphere.backend.dto.ProductResponse;
import com.shopsphere.backend.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController                    // Spring MVC (Web): returns JSON, not a web page
@RequestMapping("/api/products")   // Spring MVC (Web): base URL for this class
public class ProductController {

    private final ProductService productService;

    // Spring Core (Dependency Injection)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/products?weave=..&occasion=..&minPrice=..&maxPrice=..&inStock=true&page=0&size=12&sort=price,asc
    @GetMapping                                            // Spring MVC (Web)
    public PagedModel<ProductResponse> getProducts(
            @ModelAttribute ProductFilter filter,          // Spring MVC (data binding): URL ?weave=.. → filter.weave()
            @PageableDefault(size = 12, sort = "id")       // Spring Data (web): defaults if the URL doesn't say
            Pageable pageable) {                           // Spring Data: reads ?page= &size= &sort=
        // Spring Data (PagedModel): clean, stable JSON shape for a page
        return new PagedModel<>(productService.searchProducts(filter, pageable));
    }

    @GetMapping("/{id}")                                   // Spring MVC (Web)
    public ProductResponse getProductById(@PathVariable Long id) { // Spring MVC: {id} from the URL
        return productService.getProductById(id);
    }
}