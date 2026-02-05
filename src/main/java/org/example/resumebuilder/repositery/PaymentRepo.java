package org.example.resumebuilder.repositery;

import org.example.resumebuilder.document.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends MongoRepository<Payment,String> {
    Optional<Payment> findByrazorpayOrderId(String razorPayId);
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Payment> findByStatus(String status);
}
