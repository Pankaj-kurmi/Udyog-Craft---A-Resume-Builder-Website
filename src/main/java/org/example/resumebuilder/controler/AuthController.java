package org.example.resumebuilder.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.User;
import org.example.resumebuilder.dto.AuthResponse;
import org.example.resumebuilder.dto.LoginRequest;
import org.example.resumebuilder.dto.RegisterRequest;
import org.example.resumebuilder.service.AuthService;
import org.example.resumebuilder.service.FileUploadService;
import org.example.resumebuilder.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.example.resumebuilder.util.AppConstants.*;


@RequiredArgsConstructor
@RestController

@Slf4j
@RequestMapping(AppConstants.AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;
    private  final FileUploadService fileUploadService;
    @PostMapping(REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){

         AuthResponse response =authService.register(request);
         log.info("Response from service:{}",response);
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping(VERIFYEMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message" , "email verified succesfully"));
    }
    @PostMapping(UPLOAD_PROFILE)
    public ResponseEntity<?> uploadImage(@RequestPart("image")MultipartFile file) throws IOException {
        log.info("Inside AuthController - uploadImage()");
        Map<String,String> response = fileUploadService.uploadSingleImage(file);
        return ResponseEntity.ok(response);

    }
    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response=authService.login(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping(VALIDATE)
    public String testValidationToken(){
        return "Token validate is working";

    }

    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerification(@RequestBody Map<String,String> body){
        String email = body.get("email");

        if (Objects.isNull(email)){
            return ResponseEntity.badRequest().body(Map.of("Message" , "Email is required"));
        }

        authService.resendVerification(email);

        return  ResponseEntity.ok(Map.of("Success","true" ,"Message" ,"Verification email sent"));
    }

    @GetMapping(GET_PROFILE)
    public ResponseEntity<?> getprofile(Authentication authentication){
        String email = authentication.getName();

        AuthResponse currentProfile = authService.getProfile(email);

        return ResponseEntity.ok(currentProfile);

    }

}
