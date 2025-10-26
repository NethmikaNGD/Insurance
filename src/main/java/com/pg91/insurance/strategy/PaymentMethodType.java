package com.pg91.insurance.strategy;

/**
 * Enum representing different payment method types.
 */
public enum PaymentMethodType {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    BANK_TRANSFER("Bank Transfer"),
    DIGITAL_WALLET("Digital Wallet"),
    PAYPAL("PayPal");
    
    private final String displayName;
    
    PaymentMethodType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
