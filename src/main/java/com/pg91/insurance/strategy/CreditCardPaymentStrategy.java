package com.pg91.insurance.strategy;

import com.pg91.insurance.entity.Payment;
import org.springframework.stereotype.Component;

/**
 * Credit Card payment strategy implementation.
 */
@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {
    
    @Override
    public PaymentResult processPayment(Payment payment) throws PaymentException {
        try {
            // Simulate credit card payment processing
            System.out.println("Processing credit card payment for amount: " + payment.getAmountPaid());
            
            // Validate card details
            if (!validatePayment(payment)) {
                throw new PaymentException("Invalid credit card details", "INVALID_CARD");
            }
            
            // Simulate payment gateway call
            String transactionId = "CC_" + System.currentTimeMillis();
            
            // Simulate successful payment
            return new PaymentResult(true, transactionId, "Credit card payment processed successfully");
            
        } catch (Exception e) {
            throw new PaymentException("Credit card payment failed: " + e.getMessage(), "CC_PAYMENT_FAILED", e);
        }
    }
    
    @Override
    public PaymentMethodType getPaymentMethodType() {
        return PaymentMethodType.CREDIT_CARD;
    }
    
    @Override
    public boolean validatePayment(Payment payment) {
        if (payment == null) return false;
        
        // Basic validation for credit card
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
