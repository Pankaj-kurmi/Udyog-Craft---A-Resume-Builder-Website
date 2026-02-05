package org.example.resumebuilder.controler;

import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.Payment;
import org.example.resumebuilder.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.logging.log4j.util.StringBuilders.equalsIgnoreCase;
import static org.example.resumebuilder.util.AppConstants.PREMIUM;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/payment")
public class PaymentController {

    private PaymentService paymentService;
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String,String> request,
                                         Authentication authentication) throws RazorpayException {
        String planType=request.get("planType");
        if (!PREMIUM.equalsIgnoreCase(planType)){
            return ResponseEntity.badRequest().body(Map.of("message","Invalid plan type"));
        }
      Payment payment= paymentService.createOrder(authentication.getPrincipal(),planType);
        Map<String,Object> response = Map.of(
                "OdrerId" , payment.getRazorpayOrderId(),
                "amount"  , payment.getAmount(),
                "currency" ,payment.getCurrency(),
                "receipt" , payment.getReceipt()
        );
        return ResponseEntity.ok(response);

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String,String> request) throws RazorpayException {
      String razorpayOrderId=  request.get("razorpay_order_id");
      String razorpayPaymentId= request.get("razorpay_payment_id");
      String razorpaySinature =request.get("razorpay_signature");

        if (Objects.isNull(razorpayOrderId) ||
                Objects.isNull(razorpaySinature) ||
                Objects.isNull(razorpayPaymentId)){
            return ResponseEntity.badRequest().body(Map.of("message", "missing required payment parameters"));
            }
        boolean isValid =paymentService.verifyPayment(razorpayOrderId,razorpaySinature,razorpayPaymentId);
        if (isValid){
            return ResponseEntity.ok(Map.of(
                    "message" , "Payment verified Sucessfully",
                    "status" , "Success"

            ));

        }else {
            return ResponseEntity.ok(Map.of("message" , "Payment verification failed"));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(Authentication authentication){

         List<Payment> payments = paymentService.getUserPayments(authentication.getPrincipal());
         return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId){
        Payment paymentDetails = paymentService.getPaymentsDetail(orderId);
        return ResponseEntity.ok(paymentDetails);
    }
}
