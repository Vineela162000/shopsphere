package com.shopsphere.backend.dto;

import com.shopsphere.backend.entity.Occasion;
import java.math.BigDecimal;

// record → Java (records): a small, read-only data holder. Java writes the constructor and getters for us.
// Spring MVC (data binding): Spring fills these fields from the URL automatically.
//   Example: /api/products?weave=Soft Silk&maxPrice=10000&inStock=true
public record ProductFilter(
        String weave,          // "Soft Silk", "Mangalagiri"...
        Occasion occasion,     // Java enum: Spring turns "FESTIVE" into Occasion.FESTIVE
        String color,
        BigDecimal minPrice,   // BigDecimal → exact money math (double can give 9999.999...)
        BigDecimal maxPrice,
        Boolean inStock,       // Core Java (wrapper class): Boolean, not boolean.
        Boolean bestseller     //   null means "customer didn't pick this filter"
) {}