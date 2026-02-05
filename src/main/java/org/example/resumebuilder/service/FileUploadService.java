package org.example.resumebuilder.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.Resume;
import org.example.resumebuilder.dto.AuthResponse;
import org.example.resumebuilder.repositery.ResumeRepositry;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {
    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepositry resumeRepositry;

    public Map<String,String> uploadSingleImage(MultipartFile file) throws IOException {

       Map<String,Object>imageUploadResult =cloudinary.uploader().upload(file.getBytes() , ObjectUtils.asMap("resource_type" , "image"));
        log.info("Inside FileUploadService - uploadSingleImage() {}" , imageUploadResult.get("secure_url").toString());
       return Map.of("imageUrl",imageUploadResult.get("secure_url").toString());



    }

    public Map<String, String> uploadResumesImages(String id, @Nullable Object principal,
                                                   MultipartFile thubnail, MultipartFile profileImage) throws IOException {
      AuthResponse response = authService.getProfile((String) principal);
      Resume existingResume=resumeRepositry.findByUserIdAndId(response.getId(),id).orElseThrow(
              ()-> new RuntimeException("Resume Not Found")
      );
      Map<String,String> returnValue = new HashMap<>();
        Map<String,String> uploadResult;

     if (Objects.nonNull(thubnail)){
         uploadResult= uploadSingleImage(thubnail);
         existingResume.setThumbnaillink(uploadResult.get("imageUrl"));
         returnValue.put("thubnailLink", uploadResult.get("imageUrl"));
     }

     if (Objects.nonNull(principal)){
         uploadResult= uploadSingleImage(profileImage);
         if (Objects.isNull(existingResume.getProfileInfo())){
             existingResume.setProfileInfo( new Resume.ProfileInfo());
         }
         existingResume.getProfileInfo().setProfilePreviewUrl(uploadResult.get("imageUrl"));
         returnValue.put("ProfilePreviewUrl" , uploadResult.get("imageUrl"));
     }
     resumeRepositry.save(existingResume);
     returnValue.put("message","Images uploaded Succesfully");

     return returnValue;




    }
}
