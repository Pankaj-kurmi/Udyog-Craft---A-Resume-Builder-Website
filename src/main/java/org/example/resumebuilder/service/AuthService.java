package org.example.resumebuilder.service;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.User;
import org.example.resumebuilder.dto.AuthResponse;
import org.example.resumebuilder.dto.LoginRequest;
import org.example.resumebuilder.dto.RegisterRequest;
import org.example.resumebuilder.exceptions.ResourcesExistsException;
import org.example.resumebuilder.repositery.UserRepositry;
import org.example.resumebuilder.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepositry userRepositry;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;


    @Value("${app.base.url:http://localhost:8085}")
    private String appBaseUrl;

    public AuthResponse register(RegisterRequest request){
  log.info("Inside AuthService: register() {}" , request);
  if (userRepositry.existsByEmail(request.getEmail())){
      throw new ResourcesExistsException("user already exists with this email");
  }
       User newUser =toDocument(request);
  userRepositry.save(newUser);
  sendVerificationEmail(newUser);

  return toResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        log.info("Inside Authservice - send verificationEmail():{}", newUser);
        try {
            String link = appBaseUrl+"/api/auth/verify-email?token="+newUser.getVerificationToken();
            String html=  "<div style='font-family:sans-serif'>" +
                    "<h2>Verify your email</h2>" +
                    "<p>Hi " + newUser.getName() + ", please confirm your email to activate your account.</p>" +
                    "<p>" +
                    "<a href='" + link + "' " +
                    "style='display:inline-block;padding:10px 16px;background:#6366f1;color:#ffffff;" +
                    "border-radius:6px;text-decoration:none;font-weight:bold;'>Verify Email</a>" +
                    "</p>" +
                    "<p>Or copy this link:</p>" +
                    "<p>" + link + "</p>" +
                    "<p>This link expires in 1 hour.</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail() ,"Verify your email " , html);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    private AuthResponse toResponse(User newUser){
        return AuthResponse.builder()

                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.isEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .createAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }
    private User toDocument(RegisterRequest request){
       return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    public void verifyEmail(String token){
        User user = userRepositry.findByVerificationToken(token)
                .orElseThrow(()->new RuntimeException("Invalid or expired verifiation token"));
        if (user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verifcation token has expired. please request new one");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepositry.save(user);
    }
    public AuthResponse login(LoginRequest request){
       User existingUser= userRepositry.findByemail(request.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Invalid email address or password"));
       if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())){
           throw new UsernameNotFoundException("Invalid email or password");
       }
       if (! existingUser.isEmailVerified()){
           throw new RuntimeException("Please verify our email before login it");
       }
       String token = jwtUtil.generateToken(existingUser.getId());
          AuthResponse response =toResponse(existingUser);
          response.setToken(token);
          return response;
    }


    public void resendVerification(String email) {
       User user= userRepositry.findByemail(email)
                .orElseThrow(()-> new RuntimeException("User not found "));

       if (user.isEmailVerified()){
           throw new RuntimeException("Email is already verified");
       }

       user.setVerificationToken(UUID.randomUUID().toString());
       user.setVerificationExpires(LocalDateTime.now().plusHours(24));

       userRepositry.save(user);

       sendVerificationEmail(user);


    }

    public AuthResponse getProfile(String email) {
        User user = userRepositry.findByemail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
