package com.pg91.insurance.strategy;

/**
 * Result object containing payment processing information.
 */
public class PaymentResult {
    private boolean success;
    private String transactionId;
    private String message;
    private String errorCode;
    
    public PaymentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public PaymentResult(boolean success, String transactionId, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
    }
    
    public PaymentResult(boolean success, String message, String errorCode, boolean isError) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}