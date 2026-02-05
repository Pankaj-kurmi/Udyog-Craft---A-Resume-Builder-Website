package org.example.resumebuilder.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    @JsonProperty("_id")
    private String id;

    private String userId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorPaySignature;

    private Integer amount;
    private String currency;
    private String planType;

    @Builder.Default
    private String status= "created"; // created \paid \fail

    private String receipt;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
