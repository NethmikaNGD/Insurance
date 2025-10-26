package com.pg91.insurance.strategy;

import com.pg91.insurance.entity.Payment;
import org.springframework.stereotype.Component;

/**
 * Bank Transfer payment strategy implementation.
 */
@Component
public class BankTransferPaymentStrategy implements PaymentStrategy {
    
    @Override
    public PaymentResult processPayment(Payment payment) throws PaymentException {
        try {
            // Simulate bank transfer payment processing
            System.out.println("Processing bank transfer payment for amount: " + payment.getAmountPaid());
            
            // Validate payment details
            if (!validatePayment(payment)) {
                throw new PaymentException("Invalid bank transfer details", "INVALID_TRANSFER");
            }
            
            // Simulate bank transfer processing
            String transactionId = "BT_" + System.currentTimeMillis();
            
            // Simulate successful transfer
            return new PaymentResult(true, transactionId, "Bank transfer payment processed successfully");
            
        } catch (Exception e) {
            throw new PaymentException("Bank transfer payment failed: " + e.getMessage(), "BT_PAYMENT_FAILED", e);
        }
    }
    
    @Override
    public PaymentMethodType getPaymentMethodType() {
        return PaymentMethodType.BANK_TRANSFER;
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        if (payment == null) return false;
        
        // For bank transfer, we might not need card details
        // But we still validate basic payment info
        if (payment.getAmountPaid() == null || payment.getAmountPaid().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        return true;
    }
}
