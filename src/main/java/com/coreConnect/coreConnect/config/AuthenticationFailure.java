package com.coreConnect.coreConnect.config;
import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.coreConnect.coreConnect.helpers.Message;
import com.coreConnect.coreConnect.helpers.MessageType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        HttpSession session = request.getSession(true);
        Message message;

        if (exception instanceof DisabledException) {
            message = Message.builder().content("User is disabled!,verification email is send!!").type(MessageType.red).build();
        } else if (exception instanceof BadCredentialsException) {
            message = Message.builder().content("Invalid username or password!").type(MessageType.red).build();
        } else {
            message = Message.builder().content("Authentication failed!").type(MessageType.red).build();
        }

        session.setAttribute("message", message);
        response.sendRedirect("/login");
    }
}
