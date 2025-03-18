package com.coreConnect.coreConnect.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.coreConnect.coreConnect.services.UserService;

@Component
public class Helper {
    
    @Autowired
    private  UserService userService; // Remove static

    public  String getEmailOfLoggedInUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();
            
            String result = authentication.getName();

            if (clientId.equalsIgnoreCase("google")) {
              
                result = principal.getAttribute("email"); // Google provides direct email
            } 
            else if (clientId.equalsIgnoreCase("github")) {
            
                result = principal.getAttribute("email"); // GitHub email

                // If GitHub does not provide an email, fetch it from database using username
                if (result == null) {
                    result = userService.fetchEmailByUserName(authentication.getName());
                }
            }
            return result;
        } 
        
        // For normal email/password authentication
      
        return authentication.getName();
    }

        public static String getLinkForEmailVerfication(String emailToken){
        String link = "http://localhost:8080/auth/verify-email?token="+emailToken;
        return link;

}
}
