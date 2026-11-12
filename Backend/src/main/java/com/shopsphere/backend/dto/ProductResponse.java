package com.shopsphere.backend.dto;

import com.shopsphere.backend.entity.Occasion;
import com.shopsphere.backend.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        String category,
        String weave,
        Occasion occasion,
        String color,
        BigDecimal price,
        Integer stock,
        boolean inStock,
        String imageUrl,
        boolean bestseller) {

    // Turns a database Product into what the API shows
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.getCategory(),
                p.getWeave(),
                p.getOccasion(),
                p.getColor(),
                p.getPrice(),
                p.getStock(),
                p.getStock() > 0,
                p.getImageUrl(),
                Boolean.TRUE.equals(p.getBestseller()));
    }
}