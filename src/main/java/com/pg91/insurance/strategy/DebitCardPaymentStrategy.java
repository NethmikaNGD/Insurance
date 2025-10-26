package com.pg91.insurance.strategy;

import com.pg91.insurance.entity.Payment;
import org.springframework.stereotype.Component;

/**
 * Debit Card payment strategy implementation.
 */
@Component
public class DebitCardPaymentStrategy implements PaymentStrategy {
    
    @Override
    public PaymentResult processPayment(Payment payment) throws PaymentException {
        try {
            // Simulate debit card payment processing
            System.out.println("Processing debit card payment for amount: " + payment.getAmountPaid());
            
            // Validate card details
            if (!validatePayment(payment)) {
                throw new PaymentException("Invalid debit card details", "INVALID_CARD");
            }
            
            // Simulate payment gateway call
            String transactionId = "DC_" + System.currentTimeMillis();
            
            // Simulate successful payment
            return new PaymentResult(true, transactionId, "Debit card payment processed successfully");
            
        } catch (Exception e) {
            throw new PaymentException("Debit card payment failed: " + e.getMessage(), "DC_PAYMENT_FAILED", e);
        }
    }
    
    @Override
    public PaymentMethodType getPaymentMethodType() {
        return PaymentMethodType.DEBIT_CARD;
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        if (payment == null) return false;
        
        // Basic validation for debit card
        if (payment.getCardNo() == null || payment.getCardNo().length() < 13) {
            return false;
        }
        
        if (payment.getCardHolderName() == null || payment.getCardHolderName().trim().isEmpty()) {
            return false;
        }
        
        if (payment.getSecurityCode() == null || payment.getSecurityCode() < 100) {
            return false;
        }
        
        if (payment.getValidDate() == null) {
            return false;
        }
        
        return true;
    }
}
