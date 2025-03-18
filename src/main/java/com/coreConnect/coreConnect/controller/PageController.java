package com.coreConnect.coreConnect.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coreConnect.coreConnect.entites.User;
import com.coreConnect.coreConnect.forms.UserForm;
import com.coreConnect.coreConnect.helpers.Message;
import com.coreConnect.coreConnect.helpers.MessageType;
import com.coreConnect.coreConnect.services.ImageService;
import com.coreConnect.coreConnect.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
@Controller
public class PageController {
       @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @RequestMapping("/")
    public String index(){
        return"redirect:/home";
    }
    @RequestMapping("/home")
public String home(){
    return"home";
}
@RequestMapping("/about")
public String about(){
    return"about";
}
@RequestMapping("/services")
public String services(){
    return"services";   
}
@RequestMapping("/contact")
public String contact(){
    return"contact";   
}
@RequestMapping("/login")
public String login(){
    return"login";      
}
@RequestMapping("/register")
public String register(Model model){
    UserForm userForm = new UserForm();

    model.addAttribute("userForm", userForm);
    return"register";   

}
@RequestMapping(value = "/do-register", method = RequestMethod.POST)
public String processingRegister(@Valid @ModelAttribute("userForm") UserForm userForm ,BindingResult bindingResult,HttpSession session){
   try {
     //saving data           
    //fetching data
    System.out.println("processing user " + userForm);
    
    if(bindingResult.hasErrors()){
        System.out.println("Errors"+bindingResult);
        return"register";
    }
       User user = new User();
    user.setName(userForm.getName());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());
    user.setPhoneNumber(userForm.getPhoneNumber());
    user.setAbout(userForm.getAbout()); 
    user.setProfilePic("default.png"); 
    user.setEnabled(false);
    if(userForm.getUserImage()==null && userForm.getUserImage().isEmpty()){
        System.out.println("image is null");
    }
     if(userForm.getUserImage()!=null && !userForm.getUserImage().isEmpty()){

    String filename = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    String fileUrl = imageService.uploadImage(userForm.getUserImage(),filename);
    user.setProfilePic(fileUrl);
    user.setCloudinaryImagePublicId(filename);
 }
          User savedUser =  userService.saveUser(user);
          // adding message
          Message message= Message.builder().content("Registration Successfull !!").type(MessageType.green).build();
          session.setAttribute("message", message);
       
             return"redirect:/register";   
   } catch (Exception e) {
    session.setAttribute("message", "Registration Unsuccessfull Try Again !!");
    
             return"redirect:/register";   
   }
}
}