package com.epam.jwd.exception;

import com.epam.jwd.validator.ExpressionValidator;

public class InvalidExpressionException extends Exception {
    public InvalidExpressionException() {
        super();
    }

    public InvalidExpressionException(String message) {
        super(message);
    }
}
