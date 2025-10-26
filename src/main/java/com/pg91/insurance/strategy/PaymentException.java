package com.pg91.insurance.strategy;

/**
 * Custom exception for payment processing errors.
 */
public class PaymentException extends Exception {
    
    private String errorCode;
    
    public PaymentException(String message) {
        super(message);
    }
    
    public PaymentException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public PaymentException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
