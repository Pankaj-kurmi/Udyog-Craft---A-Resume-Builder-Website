package org.example.resumebuilder.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.Resume;
import org.example.resumebuilder.dto.AuthResponse;
import org.example.resumebuilder.dto.CreateResumeRequest;
import org.example.resumebuilder.repositery.ResumeRepositry;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {
    private final ResumeRepositry resumeRepositry;
    private final AuthService authService;


    public Resume createResume(CreateResumeRequest request, Object principalObject) {
         Resume newResume = new Resume();

        AuthResponse response= authService.getProfile((String) principalObject);

        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());

        setDefaultResumeData(newResume);

       return resumeRepositry.save(newResume);
    }

    private void setDefaultResumeData(Resume newResume) {
        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkexp(new ArrayList<>());
        newResume.setEducation(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProject(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
    }

    public List<Resume> getUserResumes(@Nullable Object principal) {
        AuthResponse response = authService.getProfile((String) principal);

        List<Resume> resumes =resumeRepositry.findByUserIdOrderByUpdatedAtDesc(response.getId());

        return resumes;
    }

    public Resume getResumeById(String resumeId, @Nullable Object principal) {
        AuthResponse response = authService.getProfile((String) principal);

       Resume existingresume =  resumeRepositry.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(()->new RuntimeException("Resume not found"));

        return existingresume;
    }

    public Resume updateResume(String id, Resume updateData, @Nullable Object principal) {
        AuthResponse response = authService.getProfile((String) principal);

        Resume existingResume = resumeRepositry.findByUserIdAndId(response.getId(), id).orElseThrow(
                ()-> new RuntimeException("Resume Not Found")
        );
        existingResume.setTitle(updateData.getTitle());
        existingResume.setThumbnaillink(updateData.getThumbnaillink());
        existingResume.setTemplates(updateData.getTemplates());
        existingResume.setProfileInfo(updateData.getProfileInfo());
        existingResume.setContactInfo(updateData.getContactInfo());
        existingResume.setWorkexp(updateData.getWorkexp());
        existingResume.setEducation(updateData.getEducation());
        existingResume.setSkills(updateData.getSkills());
        existingResume.setProject(updateData.getProject());
        existingResume.setCertifications(updateData.getCertifications());
        existingResume.setLanguages(updateData.getLanguages());
        existingResume.setInterests(updateData.getInterests());

        resumeRepositry.save(existingResume);
   return existingResume;
    }

    public void deleteResume(String id, @Nullable Object principal) {
         AuthResponse response =authService.getProfile((String) principal);

         Resume existingResume= resumeRepositry.findByUserIdAndId(response.getId(), id).
                 orElseThrow(()-> new RuntimeException("Resume not found"));

         resumeRepositry.delete(existingResume);
    }
}
