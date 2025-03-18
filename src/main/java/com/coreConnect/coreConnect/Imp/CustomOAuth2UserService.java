package com.coreConnect.coreConnect.Imp;

import java.util.Collections;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.coreConnect.coreConnect.entites.Providers;
import com.coreConnect.coreConnect.entites.User;
import com.coreConnect.coreConnect.repositories.UserRepo;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserRepo userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);
        
        // Get OAuth provider (Google/GitHub)
        String provider = userRequest.getClientRegistration().getRegistrationId();
          
        // Extract user details safely
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String profilePic = oauthUser.getAttribute("picture");
        Object idObj = oauthUser.getAttribute("id");
String providerUserId = (idObj != null) ? String.valueOf(idObj) : UUID.randomUUID().toString();
        

        // Handle missing attributes
        if (email == null || email.trim().isEmpty()) {
            if ("github".equals(provider)) {
                email = oauthUser.getAttribute("login") + "@githubuser.com";
            } else {
                logger.error("Email is missing for OAuth2 user.");
                throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
            }
        }
        if (name == null || name.trim().isEmpty()) {
            name = oauthUser.getAttribute("login"); // Fallback for GitHub users
        }
        if (profilePic == null || profilePic.trim().isEmpty()) {
            profilePic = "https://default-profile.com/default-avatar.png"; // Set a default profile pic
        }

        // Check if user exists in DB
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setUserId(UUID.randomUUID().toString()); // Unique user ID
            user.setEmail(email);
            user.setName(name);
            user.setProfilePic(profilePic);
            user.setRoleList(Collections.singletonList("ROLE_USER"));
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setProviderUserId(providerUserId);

            if ("google".equals(provider)) {
                user.setProvider(Providers.GOOGLE);
                user.setAbout("This account is created via Google OAuth2");
            } else if ("github".equals(provider)) {
                user.setProvider(Providers.GITHUB);
                user.setAbout("This account is created via GitHub OAuth2");
            }

            userRepository.save(user);
          
        } else {
            
        }

        // Set the correct username attribute key
        String userNameAttributeName = "email";
        if ("github".equals(provider)) {
            userNameAttributeName = "login"; // GitHub users are identified by "login"
        }

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oauthUser.getAttributes(),
            userNameAttributeName
        );
    }
}
