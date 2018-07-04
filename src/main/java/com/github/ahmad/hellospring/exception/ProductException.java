package com.github.ahmad.hellospring.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductException extends Exception {
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
    public ProductException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    public ProductException() {
        super();
    }
}
