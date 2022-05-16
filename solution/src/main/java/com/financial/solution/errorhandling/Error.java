package com.financial.solution.errorhandling;

import io.vavr.control.Option;

import static io.vavr.control.Option.of;

public class Error {
    private final ErrorType errorType;
    private final Option<String> errorMessage;


    public Error(ErrorType errorType,
                 String message) {
        this.errorType = errorType;
        this.errorMessage = of(message);
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Option<String> getErrorMessage() {
        return errorMessage;
    }
}
