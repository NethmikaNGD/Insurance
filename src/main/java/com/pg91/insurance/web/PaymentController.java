package com.pg91.insurance.web;

import com.pg91.insurance.entity.Payment;
import com.pg91.insurance.entity.InsurancePlan;
import com.pg91.insurance.service.PaymentService;
import com.pg91.insurance.service.InsurancePlanService;
import com.pg91.insurance.strategy.PaymentMethodType;
import com.pg91.insurance.strategy.PaymentResult;
import com.pg91.insurance.strategy.PaymentException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for handling payment-related requests.
 */
@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final InsurancePlanService insurancePlanService;

    public PaymentController(PaymentService paymentService, InsurancePlanService insurancePlanService) {
        this.paymentService = paymentService;
        this.insurancePlanService = insurancePlanService;
    }

    /**
     * Show payment form for a specific insurance plan.
     *
     * @param planId The plan ID
     * @param model The model to add attributes
     * @param session The HTTP session
     * @return Payment form view
     */
    @GetMapping("/payment")
    public String showPaymentForm(@RequestParam("planId") Integer planId,
                                  Model model,
                                  HttpSession session) {
        try {
            // Get current user ID from session
            Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
            if (userId == null) {
                return "redirect:/login";
            }

            // Get insurance plan details
            InsurancePlan plan = insurancePlanService.getActivePlanById(planId);
            if (plan == null) {
                model.addAttribute("error", "Insurance plan not found or not active");
                return "redirect:/plans";
            }

            // Create payment object with plan details
            Payment payment = new Payment();
            payment.setAppUserId(userId);
            payment.setPlanId(planId);
            payment.setAmountPaid(plan.getPriceMonth()); // Default to monthly price

            // Add attributes to model
            model.addAttribute("payment", payment);
            model.addAttribute("plan", plan);
            model.addAttribute("availablePaymentMethods", paymentService.getAvailablePaymentMethods());
            model.addAttribute("selectedDuration", "month"); // Default

            return "payment";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading payment form: " + e.getMessage());
            return "redirect:/plans";
        }
    }

    /**
     * Process payment submission.
     *
     * @param payment The payment object
     * @param result The binding result
     * @param paymentMethod The selected payment method
     * @param model The model
     * @param session The HTTP session
     * @param redirectAttributes The redirect attributes
     * @return Redirect to appropriate page
     */
    @PostMapping("/processPayment")
    public String processPayment(@Valid @ModelAttribute Payment payment,
                                 BindingResult result,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        try {
            // Get current user ID from session
            Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
            if (userId == null) {
                return "redirect:/login";
            }

            // Set user ID if not already set
            if (payment.getAppUserId() == null) {
                payment.setAppUserId(userId);
            }

            // Get plan for error re-render
            InsurancePlan plan = insurancePlanService.getActivePlanById(payment.getPlanId());
            if (plan == null) {
                model.addAttribute("error", "Insurance plan not found");
                return "redirect:/plans";
            }

            // Infer selected duration for error re-render
            String selectedDuration = inferSelectedDuration(payment.getAmountPaid(), plan);
            model.addAttribute("selectedDuration", selectedDuration);

            // Validate form
            if (result.hasErrors()) {
                model.addAttribute("plan", plan);
                model.addAttribute("availablePaymentMethods", paymentService.getAvailablePaymentMethods());
                model.addAttribute("error", "Please correct the form errors");
                return "payment";
            }

            // Parse payment method
            PaymentMethodType methodType;
            try {
                methodType = PaymentMethodType.valueOf(paymentMethod.toUpperCase());
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Invalid payment method selected");
                model.addAttribute("plan", plan);
                model.addAttribute("availablePaymentMethods", paymentService.getAvailablePaymentMethods());
                return "payment";
            }

            // Process payment
            PaymentResult paymentResult = paymentService.processPayment(payment, methodType);

            if (paymentResult.isSuccess()) {
                redirectAttributes.addFlashAttribute("success",
                        "Payment processed successfully! Transaction ID: " + paymentResult.getTransactionId());
                return "redirect:/plans";
            } else {
                model.addAttribute("error", "Payment failed: " + paymentResult.getMessage());
                model.addAttribute("plan", plan);
                model.addAttribute("availablePaymentMethods", paymentService.getAvailablePaymentMethods());
                return "payment";
            }

        } catch (PaymentException e) {
            model.addAttribute("error", "Payment error: " + e.getMessage());
            InsurancePlan plan = insurancePlanService.getActivePlanById(payment.getPlanId());
            String selectedDuration = inferSelectedDuration(payment.getAmountPaid(), plan);
            model.addAttribute("selectedDuration", selectedDuration != null ? selectedDuration : "month");
            model.addAttribute("plan", plan);
            model.addAttribute("availablePaymentMethods", paymentService.getAvailablePaymentMethods());
            return "payment";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
            InsurancePlan plan = insurancePlanService.getActivePlanById(payment.getPlanId());
            String selectedDuration = inferSelectedDuration(payment.getAmountPaid(), plan);
            model.addAttribute("selectedDuration", selectedDuration != null ? selectedDuration : "month");
            model.addAttribute("plan", plan);
            model.addAttribute("availablePaymentMethods", paymentService.getAvailablePaymentMethods());
            return "payment";
        }
    }

    /**
     * Infer selected duration from amount paid.
     */
    private String inferSelectedDuration(BigDecimal amount, InsurancePlan plan) {
        if (amount == null || plan == null) return "month";
        if (amount.compareTo(plan.getPrice6m()) == 0) return "6m";
        if (amount.compareTo(plan.getPriceYear()) == 0) return "year";
        return "month";
    }

    /**
     * Show payment history for the current user.
     *
     * @param model The model
     * @param session The HTTP session
     * @return Payment history view
     */
    @GetMapping("/paymentHistory")
    public String showPaymentHistory(Model model, HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
            if (userId == null) {
                return "redirect:/login";
            }

            List<Payment> payments = paymentService.getPaymentsByUserId(userId);
            BigDecimal totalPaid = paymentService.getTotalAmountPaidByUser(userId);

            model.addAttribute("payments", payments);
            model.addAttribute("totalPaid", totalPaid);

            return "paymentHistory";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading payment history: " + e.getMessage());
            return "redirect:/plans";
        }
    }
}