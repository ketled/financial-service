package com.financial.solution.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.financial.solution.validator.RequestValidator.validation;
import static org.assertj.core.api.Assertions.assertThat;

class RequestValidatorTest {

    @Test
    @DisplayName("Should return validation error for isin")
    public void testValidationForBlankIsin() {
        assertThat(validation("", "", "").isInvalid()).isTrue();
    }

    @Test
    @DisplayName("Should return valid response for as isin is provided")
    public void testValidationForInputIsin() {
        assertThat(validation("BMG9156K1018", "", "").isValid()).isTrue();
    }

    @Test
    @DisplayName("Should return validation error for invalid from date")
    public void testValidationForInvalidFromDate() {
        assertThat(validation("BMG9156K1018", "123", "").isInvalid()).isTrue();
    }

    @Test
    @DisplayName("Should return validation error for invalid to date")
    public void testValidationForInvalidToDate() {
        assertThat(validation("BMG9156K1018", "", "abc").isInvalid()).isTrue();
    }

    @Test
    @DisplayName("All validation should be passed successfully")
    public void testValidationSuccess() {
        assertThat(validation("BMG9156K1018", "2022-05-02", "2022-06-02").isValid()).isTrue();
    }

}