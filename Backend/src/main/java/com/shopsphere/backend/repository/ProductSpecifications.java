package com.shopsphere.backend.repository;

import com.shopsphere.backend.dto.ProductFilter;
import com.shopsphere.backend.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Core Java (utility class): "final" + private constructor = nobody creates objects of it,
// we only call its static method: ProductSpecifications.withFilter(filter)
public final class ProductSpecifications {

    private ProductSpecifications() {}

    // Spring Data JPA (Specification): "a rule for the WHERE clause"
    public static Specification<Product> withFilter(ProductFilter filter) {

        // Java 8 (lambda + functional interface): Specification has ONE method,
        // so we can write it as a short lambda instead of a full class.
        // root = the products table, cb = the tool that builds SQL conditions
        return (root, query, cb) -> {

            // Java Collections (List / ArrayList): a growing list of conditions
            List<Predicate> rules = new ArrayList<>();

            // Always: hide sarees the admin turned off
            rules.add(cb.isTrue(root.<Boolean>get("active")));

            if (filter.weave() != null && !filter.weave().isBlank()) {
                // lower() on both sides → "soft silk" matches "Soft Silk"
                rules.add(cb.equal(cb.lower(root.<String>get("weave")), filter.weave().toLowerCase()));
            }
            if (filter.occasion() != null) {
                rules.add(cb.equal(root.get("occasion"), filter.occasion()));
            }
            if (filter.color() != null && !filter.color().isBlank()) {
                rules.add(cb.equal(cb.lower(root.<String>get("color")), filter.color().toLowerCase()));
            }
            if (filter.minPrice() != null) {
                rules.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("price"), filter.minPrice()));
            }
            if (filter.maxPrice() != null) {
                rules.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("price"), filter.maxPrice()));
            }
            // Boolean.TRUE.equals(...) → safe even when the value is null (no NullPointerException)
            if (Boolean.TRUE.equals(filter.inStock())) {
                rules.add(cb.greaterThan(root.<Integer>get("stock"), 0));
            }
            if (Boolean.TRUE.equals(filter.bestseller())) {
                rules.add(cb.isTrue(root.<Boolean>get("bestseller")));
            }

            // Join all rules with AND
            return cb.and(rules.toArray(new Predicate[0]));
        };
    }
}