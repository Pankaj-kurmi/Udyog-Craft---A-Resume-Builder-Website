package org.example.resumebuilder.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.Payment;
import org.example.resumebuilder.document.User;
import org.example.resumebuilder.dto.AuthResponse;
import org.example.resumebuilder.repositery.PaymentRepo;
import org.example.resumebuilder.repositery.UserRepositry;
import org.json.JSONObject;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.UUID;

import static org.example.resumebuilder.util.AppConstants.PREMIUM;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final AuthService authService;
    private final UserRepositry userRepositry;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public Payment createOrder(@Nullable Object principal, String planType) throws RazorpayException {
        AuthResponse authResponse = authService.getProfile((String) principal);

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId,razorpayKeySecret);

        int amount =4900; // Amount is in paise that means 49 rupees
        String currency = "INR";
        String receipt =  PREMIUM+ UUID.randomUUID().toString().substring(0,8);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency",currency);
        orderRequest.put("receipt",receipt);

        Order razorpayOrder= razorpayClient.orders.create(orderRequest);

        Payment newPayment =Payment.builder()
                .userId(authResponse.getId())
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(amount)
                .currency(currency)
                .planType(planType)
                .status("created")
                .receipt(receipt)
                .build();

      return   paymentRepo.save(newPayment);

    }

    public boolean verifyPayment(String razorpayOrderId, String razorpaySinature, String razorpayPaymentId) throws RazorpayException {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("raqzorpay_order_id" , razorpayOrderId);
            attributes.put("razorpay_payment_id" , razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySinature);

            boolean isValidSignature = Utils.verifyPaymentSignature(attributes , razorpayKeySecret);
            if (isValidSignature){
                Payment payment =paymentRepo.findByrazorpayOrderId(razorpayOrderId)
                        .orElseThrow(()-> new RuntimeException("Payment not found"));
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorPaySignature(razorpaySinature);
                payment.setStatus("paid");
                paymentRepo.save(payment);

                upgradeUserSubscription(payment.getUserId() ,payment.getPlanType());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error in Verifying the payment:" ,e);
            return false;

        }
    }

    private void upgradeUserSubscription(String userId, String planType) {
        User existingUser=userRepositry.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        existingUser.setSubscriptionPlan(planType);
        userRepositry.save(existingUser);
        log.info("User {} upgraded to {} plan", userId ,planType);

    }

    public List<Payment> getUserPayments(@Nullable Object principal) {
      AuthResponse authResponse=  authService.getProfile((String) principal);
      return paymentRepo.findByUserIdOrderByCreatedAtDesc(authResponse.getId());
    }

    public Payment getPaymentsDetail(String orderId) {
     return    paymentRepo.findByrazorpayOrderId(orderId)
                .orElseThrow(()-> new RuntimeException("Payment not found"));
    }
}
