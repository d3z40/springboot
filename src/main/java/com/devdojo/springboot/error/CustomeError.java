package com.devdojo.springboot.error;

public class CustomeError {
    private String errorMessage;

    public CustomeError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
