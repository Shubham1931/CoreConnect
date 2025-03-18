package com.coreConnect.coreConnect.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
// UserController class is created to handle the user related requests
public class UserController {
    
    //user dashboard
    @RequestMapping(value="/dashboard",method=RequestMethod.GET)
// dashboard method is created to handle the user dashboard request
public String dashboard(){
    return "user/dashboard";
}
@RequestMapping(value = "/profile", method=RequestMethod.GET)
public String requestMethodName(Authentication authentication, Model model) {
   
    return "user/profile";
}

//user add contact page
//user add contact submit
//user view contact page   
}
