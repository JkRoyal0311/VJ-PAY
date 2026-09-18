package com.coforge.controllers;

import com.coforge.dtos.PaymentOrderRequest;
import com.coforge.dtos.PaymentOrderResponse;
import com.coforge.dtos.PaymentVerifyRequest;
import com.coforge.services.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentOrderRequest request) {
        try {
            PaymentOrderResponse response = paymentService.createOrder(request);
            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerifyRequest request) {
        boolean isSignatureValid = paymentService.verifyPaymentSignature(request);
        if (isSignatureValid) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payment verified successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Invalid payment signature");
        }
    }
}
