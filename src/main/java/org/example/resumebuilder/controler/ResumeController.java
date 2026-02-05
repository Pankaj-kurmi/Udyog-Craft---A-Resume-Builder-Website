package org.example.resumebuilder.controler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.Resume;
import org.example.resumebuilder.dto.CreateResumeRequest;
import org.example.resumebuilder.service.FileUploadService;
import org.example.resumebuilder.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.example.resumebuilder.util.AppConstants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(RESUME)
public class ResumeController {
    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request,
                                          Authentication authentication){
        Resume newResume =resumeService.createResume(request,authentication.getPrincipal());

        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);

    }
    @GetMapping
    public ResponseEntity<?> getuserResume(Authentication authentication){
        List<Resume> resumes = resumeService.getUserResumes(authentication.getPrincipal());

        return ResponseEntity.ok(resumes);
    }
    @GetMapping(ID)
    public ResponseEntity<?> getresumeById(@PathVariable String id , Authentication authentication){
        Resume existingResume = resumeService.getResumeById(id ,authentication.getPrincipal());
        return ResponseEntity.ok(existingResume);

    }
    @PutMapping(ID)
    public ResponseEntity<?> updateresume(@PathVariable String id , @RequestBody Resume updateData,
                                          Authentication authentication){
        Resume updatedResume=resumeService.updateResume(id, updateData , authentication.getPrincipal());

        return ResponseEntity.ok(updatedResume);
    }

    @PutMapping(UPLOAD_IMAGE)
    public ResponseEntity<?> uploadResumeImages(@PathVariable String id, @RequestPart(value = "thumbnail" , required = false)
                                                MultipartFile thubnail , @RequestPart(value = "profileImage", required = false)
                                                MultipartFile profileImage, HttpServletRequest request, Authentication authentication) throws IOException {
        Map<String,String> response = fileUploadService.uploadResumesImages(id, authentication.getPrincipal(),thubnail,profileImage);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping(ID)
    public ResponseEntity<?> deleteResume(@PathVariable String id , Authentication authentication){

        resumeService.deleteResume(id,authentication.getPrincipal());

        return ResponseEntity.ok(Map.of("Message", "Resume deleted Successfully"));

    }
}
