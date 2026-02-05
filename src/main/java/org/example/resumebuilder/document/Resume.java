package org.example.resumebuilder.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document( collection = "resumes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Resume {
    @Id
    @JsonProperty("_id")
    private String id;

    private String userId;

    private String Title;

    private String thumbnaillink;

    private String templates;

    private ProfileInfo profileInfo;

    private ContactInfo contactInfo;

    private List<WorkExperience> workexp;

    private List<Education> education;

    private List<Skills> skills;

    private  List<project> Project;

    private List<Certification> certifications;

    private List<Languages>languages;

    private List<String> interests;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static class Template{
        private String theme;
        private List<String> colorpalattes;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder

    public static class ProfileInfo{
        private String profilePreviewUrl;
        private String fullname;
        private String designiation;
        private String summary;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ContactInfo{
        private String email;
        private String location;
        private String phone;
        private String linkedin;
        private String github;
        private String website;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WorkExperience {
        private String company;
        private String role;
        private String startdate;
        private String enddate;
        private String description;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Education{
        private String degree;
        private String Instution;
        private String startDate;
        private String endDate;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Skills{
        private String name;
        private String progress;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class project{
        private String title;
        private String description;
        private String githublink;
        private String liveDemo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Certification{
        private String title;
        private String issuer;
        private String Year;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Languages{
        private String name;
        private Integer progress;
    }
}


