package com.coreConnect.coreConnect.controller;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.coreConnect.coreConnect.config.AppConfig;
import com.coreConnect.coreConnect.config.OAuthAuthenticationSuccessHandler;
import com.coreConnect.coreConnect.entites.Contact;
import com.coreConnect.coreConnect.entites.User;
import com.coreConnect.coreConnect.forms.ContactForm;
import com.coreConnect.coreConnect.helpers.AppConstants;
import com.coreConnect.coreConnect.helpers.Helper;
import com.coreConnect.coreConnect.helpers.Message;
import com.coreConnect.coreConnect.helpers.MessageType;
import com.coreConnect.coreConnect.services.ContactService;
import com.coreConnect.coreConnect.services.ImageService;
import com.coreConnect.coreConnect.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private final AuthenticationProvider authenticationProvider;

    private final AppConfig appConfig;

    private final ApiController apiController;

    private final UserController userController;

    private final OAuthAuthenticationSuccessHandler OAuthAuthenticationSuccessHandler;
    @Autowired
    private ContactService contactService;
     @Autowired
    private Helper helper;
     @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;


    ContactController(OAuthAuthenticationSuccessHandler OAuthAuthenticationSuccessHandler, UserController userController, ApiController apiController, AppConfig appConfig, AuthenticationProvider authenticationProvider) {
        this.OAuthAuthenticationSuccessHandler = OAuthAuthenticationSuccessHandler;
        this.userController = userController;
        this.apiController = apiController;
        this.appConfig = appConfig;
        this.authenticationProvider = authenticationProvider;
    }


@RequestMapping("/add")
    public String contact(Model modal ) {
               ContactForm contactForm = new ContactForm();
                                      modal.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }   

@PostMapping("/add")
public String addContact( @ModelAttribute ContactForm contactForm,Authentication authentication,BindingResult bindingResult,HttpSession session){
       System.out.println(contactForm);
    Contact contact = new Contact();
    String userName = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.fetchUserByEmail(userName);
      
    contact.setContactName(contactForm.getContactName());
    contact.setContactEmail(contactForm.getContactEmail());
    contact.setFavrouite(contactForm.isFavrouite());
    contact.setContactPhone(contactForm.getContactPhone());
    contact.setDescription(contactForm.getDescription());
    contact.setLinkedInLink(contactForm.getLinkedInLink());
    contact.setWebsiteLink(contactForm.getWebsiteLink());
    contact.setAddress(contactForm.getAddress());
    contact.setUser(user);
 if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){

    String filename = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    String fileUrl = imageService.uploadImage(contactForm.getContactImage(),filename);
    contact.setPicture(fileUrl);
    contact.setCloudinaryImagePublicId(filename);
 }
if(contactForm.getContactName()!=""){
    contactService.save(contact);
    session.setAttribute("message", Message.builder()
    .content("Contact added successfully")
    .type(MessageType.green)
    .build());
}else{
    session.setAttribute("message", Message.builder()
    .content("Fill required informations")
    .type(MessageType.green)
    .build());
}
   
    return "redirect:/user/contacts/add";
}
@RequestMapping
 public String viewContacts(@RequestParam(value = "page",defaultValue = "0") int page,
 @RequestParam( value =  "size",defaultValue = "4") int size,
 @RequestParam(value = "sortBy",defaultValue = "contactName") String sortBy,
 @RequestParam(value = "direction",defaultValue = "asc") String direction,

    Model model,Authentication authentication){
    String userName = helper.getEmailOfLoggedInUser(authentication);
    User user = userService.fetchUserByEmail(userName);
    Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);
    model.addAttribute("pageContact", pageContact);
    model.addAttribute("pageSize",AppConstants.PAGE_SIZE);

    return "user/contacts";
 }
 // search handler
 @RequestMapping("/search")
 public String searchHandler(
     @RequestParam("field") String field,
     @RequestParam("keyword") String value,
     @RequestParam(value = "page", defaultValue = "0") int page,
     @RequestParam(value = "size", defaultValue = "8") int size,
     @RequestParam(value = "sortBy", defaultValue = "contactName") String sortBy,
     @RequestParam(value = "direction", defaultValue = "asc") String direction,
     Model model,Authentication authentication) {
 
     Page<Contact> pageContact = null;
     String userName = helper.getEmailOfLoggedInUser(authentication);
     User user = userService.fetchUserByEmail(userName);
     if (field.equalsIgnoreCase("ContactName")) {
         pageContact = contactService.searchByContactName(value, page, size, sortBy, direction,user);
     } 
     else if (field.equalsIgnoreCase("ContactEmail")) {
         pageContact = contactService.searchByContactEmail(value, page, size, sortBy, direction,user);
     } 
     else {
         pageContact = contactService.searchByContactPhone(value, page, size, sortBy, direction,user);
     }
 
     model.addAttribute("pageContact", pageContact);
     System.out.println(pageContact);
 
     return "user/search";
 }
@RequestMapping("/delete/{id}")
public String deleteContact(@PathVariable("id") String contactId, RedirectAttributes redirectAttributes) {
    try {
        contactService.delete(contactId); // Call service to delete
        redirectAttributes.addFlashAttribute("successMessage", "Contact deleted successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error deleting contact.");
    }
    return "redirect:/user/contacts"; // Redirect back to contacts list
}


@RequestMapping("/view/{id}")
public String updateContact(@PathVariable("id") String contactId, Model model, RedirectAttributes redirectAttributes) {
  var contact =   contactService.getById(contactId);
    ContactForm contactForm = new ContactForm();
    contactForm.setContactName(contact.getContactName());
    contactForm.setContactEmail(contact.getContactEmail());
    contactForm.setContactPhone(contact.getContactPhone());
    contactForm.setAddress(contact.getAddress());
    contactForm.setDescription(contact.getDescription());
    contactForm.setFavrouite(contact.isFavrouite());
    contactForm.setWebsiteLink(contact.getWebsiteLink());
    contactForm.setLinkedInLink(contact.getLinkedInLink());
    contactForm.setWebsiteLink(contact.getWebsiteLink());
    contactForm.setPicture(contact.getPicture());
    model.addAttribute("contact",contactForm);
    model.addAttribute("contactId", contactId);
    return "user/update"; // Load the update page with contact details
}





@PostMapping("/update/{contactId}")
public String updateContactDetails(@PathVariable("contactId") String contactId,
                                   @ModelAttribute ContactForm contactForm,
                                   Model model) {
    var con = contactService.getById(contactId);

    // Update fields
    con.setContactId(contactId);
    con.setContactName(contactForm.getContactName());
    con.setContactEmail(contactForm.getContactEmail());
    con.setContactPhone(contactForm.getContactPhone());
    con.setAddress(contactForm.getAddress());
    con.setDescription(contactForm.getDescription());
    con.setFavrouite(contactForm.isFavrouite());
    con.setWebsiteLink(contactForm.getWebsiteLink());
    con.setLinkedInLink(contactForm.getLinkedInLink());

    System.out.println("Existing Image: " + con.getPicture()); // Debug log

    // If a new image is selected, upload and update the picture
    if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
        String filename = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        String fileUrl = imageService.uploadImage(contactForm.getContactImage(), filename);
        con.setCloudinaryImagePublicId(filename);
        con.setPicture(fileUrl);
        System.out.println("New Image Uploaded: " + fileUrl); // Debug log
    } else {
        // **Keep the existing image if no new one is uploaded**
        System.out.println("No new image selected, retaining old image."); // Debug log
    }

    contactService.update(con);
    return "redirect:/user/contacts"; // Redirect to contacts list after update
}



}
