package com.shopsphere.backend.dto;

import com.shopsphere.backend.entity.Occasion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// Java (record): the JSON the admin sends to add or edit a saree
// Jakarta Bean Validation: each annotation is a rule. Broken rule → 400 Bad Request
public record ProductRequest(
        @NotBlank @Pattern(regexp = "SAR-\\d{4}") String sku,   // must look like SAR-0013
        @NotBlank @Size(max = 255) String name,                   // not empty, not too long
        String description,                                       // optional
        @NotBlank String category,
        @NotBlank String weave,
        @NotNull Occasion occasion,                               // EVERYDAY, FESTIVE or BRIDAL
        @NotBlank String color,
        @NotNull @Positive BigDecimal price,                      // TDD: NEW rule, price must be more than 0
        @NotNull @PositiveOrZero Integer stock,                   // 0 is allowed (out of stock)
        String imageUrl,                                          // optional, photos come later
        Boolean bestseller                                        // optional, null = false
) {}