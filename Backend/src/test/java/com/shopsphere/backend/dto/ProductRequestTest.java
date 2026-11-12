package com.shopsphere.backend.dto;

import com.shopsphere.backend.entity.Occasion;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// JUnit 5 (unit test): tests one small thing, no server, no database, runs in milliseconds
// TDD: this test is written BEFORE ProductRequest exists
class ProductRequestTest {

    // Jakarta Bean Validation: the same checker Spring uses on incoming JSON
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // Helper: a valid saree, only the price changes
    private ProductRequest sareeWithPrice(String price) {
        return new ProductRequest("SAR-0099", "Test saree", "A saree for testing", "Sarees",
                "Soft Silk", Occasion.FESTIVE, "Red", new BigDecimal(price), 5, null, false);
    }

    @Test // JUnit 5: marks a test method
    void priceOfZeroIsRejected() {
        Set<ConstraintViolation<ProductRequest>> errors = validator.validate(sareeWithPrice("0"));
        // AssertJ: readable checks. "the errors should mention the price field"
        assertThat(errors).extracting(e -> e.getPropertyPath().toString()).contains("price");
    }

    @Test
    void negativePriceIsRejected() {
        Set<ConstraintViolation<ProductRequest>> errors = validator.validate(sareeWithPrice("-100"));
        assertThat(errors).extracting(e -> e.getPropertyPath().toString()).contains("price");
    }

    @Test
    void positivePriceIsAccepted() {
        assertThat(validator.validate(sareeWithPrice("2500"))).isEmpty();
    }
}