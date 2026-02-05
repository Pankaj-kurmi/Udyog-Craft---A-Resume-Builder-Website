package org.example.resumebuilder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    @Size(min = 1  , max = 100 , message = "Name must be in between 1 and 100 characters")
    private String name;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 15 , message = "password must in between 6 and 15 characters")
    private String password;

    private String profileImageUrl;


}
