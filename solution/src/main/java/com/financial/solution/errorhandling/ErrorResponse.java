package com.financial.solution.errorhandling;

import org.springframework.http.ResponseEntity;

import static java.util.List.of;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

public class ErrorResponse {
    public static ResponseEntity<?> respondError(final Error error) {
        return switch (error.getErrorType()) {
            case INVALID_INPUT, VALIDATION_FAILED, API_FAILURE -> badRequest()
                    .body(of(new HttpErrorResponse(error.getErrorType().name(), error.getErrorMessage().getOrElse(""))));
            default -> status(INTERNAL_SERVER_ERROR).build();
        };
    }
}
