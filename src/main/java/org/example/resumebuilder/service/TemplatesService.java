package org.example.resumebuilder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.dto.AuthResponse;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.example.resumebuilder.util.AppConstants.PREMIUM;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {
    private final AuthService authService;

    public Map<String,Object> getTemplates(Object principal){
        AuthResponse response = authService.getProfile((String) principal);


        List<String> availableTemplates;

        Boolean ispremium = PREMIUM.equalsIgnoreCase(response.getSubscriptionPlan());
        if (ispremium){
            availableTemplates= List.of("01","02","03");
        }
        else {
            availableTemplates= List.of("01");

        }
        Map<String,Object> restrictions = new HashMap<>();
        restrictions.put("availabletemplates",availableTemplates);
        restrictions.put("allTemplates" , List.of("01","02","03"));
        restrictions.put("subscritionplan", response.getSubscriptionPlan());
        restrictions.put("isPremium" ,ispremium);

     return restrictions;
    }
}
