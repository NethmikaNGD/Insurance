package com.pg91.insurance.strategy;

import com.pg91.insurance.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Payment Context class that manages different payment strategies.
 * This class implements the Strategy Pattern to handle different payment methods.
 */
@Service
public class PaymentContext {
    
    private final Map<PaymentMethodType, PaymentStrategy> strategies;
    
    @Autowired
    public PaymentContext(List<PaymentStrategy> strategyList) {
        this.strategies = new HashMap<>();
        
        // Initialize strategies map
        for (PaymentStrategy strategy : strategyList) {
            strategies.put(strategy.getPaymentMethodType(), strategy);
        }
    }
    
    /**
     * Process payment using the specified payment method.
     * 
     * @param payment The payment object
     * @param paymentMethod The payment method type
     * @return PaymentResult containing the result
     * @throws PaymentException if payment processing fails
     */
    public PaymentResult processPayment(Payment payment, PaymentMethodType paymentMethod) throws PaymentException {
        PaymentStrategy strategy = strategies.get(paymentMethod);
        
        if (strategy == null) {
            throw new PaymentException("Unsupported payment method: " + paymentMethod, "UNSUPPORTED_METHOD");
        }
        
        return strategy.processPayment(payment);
    }
    
    /**
     * Get available payment methods.
     * 
     * @return Array of available payment method types
     */
    public PaymentMethodType[] getAvailablePaymentMethods() {
        return strategies.keySet().toArray(new PaymentMethodType[0]);
    }
    
    /**
     * Check if a payment method is supported.
     * 
     * @param paymentMethod The payment method to check
     * @return true if supported, false otherwise
     */
    public boolean isPaymentMethodSupported(PaymentMethodType paymentMethod) {
        return strategies.containsKey(paymentMethod);
    }
    
    /**
     * Validate payment using the specified payment method.
     * 
     * @param payment The payment object
     * @param paymentMethod The payment method type
     * @return true if validation passes, false otherwise
     */
    public boolean validatePayment(Payment payment, PaymentMethodType paymentMethod) {
        PaymentStrategy strategy = strategies.get(paymentMethod);
        
        if (strategy == null) {
            return false;
        }
        
        return strategy.validatePayment(payment);
    }
}
