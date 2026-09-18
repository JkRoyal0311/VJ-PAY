package com.coforge.services;

import com.coforge.dtos.PaymentOrderRequest;
import com.coforge.dtos.PaymentOrderResponse;
import com.coforge.dtos.PaymentVerifyRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

    private static final String KEY_ID = "rzp_test_SjC6NyPbfuyIi3";
    private static final String KEY_SECRET = "8K4LRD6WQYirIHPOV6D7ODTn";

    public PaymentOrderResponse createOrder(PaymentOrderRequest request) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(KEY_ID, KEY_SECRET);

        JSONObject orderRequest = new JSONObject();
        // Razorpay expects amount in paise (multiply by 100)
        int amountInPaise = request.getAmount().multiply(new BigDecimal("100")).intValue();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpay.orders.create(orderRequest);

        return new PaymentOrderResponse(
                order.get("id"),
                request.getAmount(),
                order.get("currency"),
                KEY_ID);
    }

    public boolean verifyPaymentSignature(PaymentVerifyRequest request) {
        try {
            String generatedSignature = calculateRFC2104HMAC(
                    request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(),
                    KEY_SECRET);
            return generatedSignature.equals(request.getRazorpaySignature());
        } catch (Exception e) {
            return false;
        }
    }

    private String calculateRFC2104HMAC(String data, String secret) throws java.security.SignatureException {
        String result;
        try {
            // get an hmac_sha256 key from the raw secret bytes
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            // get an hmac_sha256 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // base64-encode the hmac
            result = bytesToHex(rawHmac);
        } catch (Exception e) {
            throw new java.security.SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
