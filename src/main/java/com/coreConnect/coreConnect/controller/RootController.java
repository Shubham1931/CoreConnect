package com.coreConnect.coreConnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.coreConnect.coreConnect.entites.User;
import com.coreConnect.coreConnect.helpers.Helper;
import com.coreConnect.coreConnect.services.ContactService;
import com.coreConnect.coreConnect.services.UserService;

@ControllerAdvice
public class RootController {
        @Autowired
    private Helper helper;
     @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;
            @ModelAttribute
public void addLoggedInUserInformation(Model model, Authentication authentication) {
    if(authentication == null) {
        return;
    }
    String userName = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.fetchUserByEmail(userName);
      long totalcontacts = contactService.getTotalContactsByUserEmail(userName);
model.addAttribute("totalContacts",totalcontacts);
   model.addAttribute("loggedInUser", user);


}
}
