package com.pg91.insurance.strategy;

import com.pg91.insurance.entity.Payment;

/**
 * Strategy interface for different payment methods.
 * This interface defines the contract for processing payments using different strategies.
 */
public interface PaymentStrategy {
    
    /**
     * Process payment using the specific payment method strategy.
     * 
     * @param payment The payment object containing payment details
     * @return PaymentResult containing the result of the payment processing
     * @throws PaymentException if payment processing fails
     */
    PaymentResult processPayment(Payment payment) throws PaymentException;
    
    /**
     * Get the payment method type supported by this strategy.
     * 
     * @return PaymentMethodType enum value
     */
    PaymentMethodType getPaymentMethodType();
    
    /**
     * Validate payment details specific to this payment method.
     * 
     * @param payment The payment object to validate
     * @return true if validation passes, false otherwise
     */
    boolean validatePayment(Payment payment);
}
