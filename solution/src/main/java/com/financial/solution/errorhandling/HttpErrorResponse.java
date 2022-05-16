package com.financial.solution.errorhandling;

public class HttpErrorResponse {
    private final String errorCode;
    private final String errorMessage;

    public HttpErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
