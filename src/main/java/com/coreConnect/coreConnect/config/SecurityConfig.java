package com.coreConnect.coreConnect.config;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.coreConnect.coreConnect.Imp.CustomOAuth2UserService;
import com.coreConnect.coreConnect.Imp.CustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails user1 = User.withUsername("admin123")
    //             .password(passwordEncoder().encode("admin123")) // Correct password encryption
    //             .roles("ADMIN", "USER") // ✅ Corrected syntax
    //             .build();

    //     UserDetails user2 = User.withUsername("user123")
    //             .password(passwordEncoder().encode("user123")) // Correct password encryption
    //             .roles("USER") // ✅ Corrected syntax
    //             .build();

    //     return new InMemoryUserDetailsManager(user1, user2);
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder(); // ✅ Correct way to encrypt passwords
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable()) // Disable CSRF for testing (enable in production)
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/user/**").hasRole("USER") // Only ADMIN can access /admin/**
    //             .anyRequest().authenticated() // All other requests require authentication
    //         )
    //         .formLogin(login -> login
    //             .defaultSuccessUrl("/home", true) // Redirect after successful login
    //             .permitAll()
    //         )
    //         .logout(logout -> logout
    //             .logoutUrl("/logout")
    //             .logoutSuccessUrl("/login?logout")
    //             .permitAll()
    //         );

    //     return http.build();
    // }
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private AuthenticationFailure authenticationFailure;
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

        
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing (enable in production)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").hasRole("USER")// Only ADMIN can access /admin/**
                .anyRequest().permitAll() // All other requests require authentication
            )
            .formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .failureHandler(authenticationFailure)
            .usernameParameter("email")
            .passwordParameter("password")
            
                .defaultSuccessUrl("/user/profile", true) // Redirect after successful login
                .permitAll()                                                                                                                                                    
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .oauth2Login(oauth -> oauth
            .loginPage("/login") 
          
             .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService))
                .successHandler(oAuthAuthenticationSuccessHandler)
             
            );          
     
        return http.build();
    }
}
