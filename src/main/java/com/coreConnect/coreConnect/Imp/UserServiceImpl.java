package com.coreConnect.coreConnect.Imp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coreConnect.coreConnect.entites.User;
import com.coreConnect.coreConnect.helpers.AppConstants;
import com.coreConnect.coreConnect.helpers.Helper;
import com.coreConnect.coreConnect.helpers.ResourceNotFoundException;
import com.coreConnect.coreConnect.repositories.UserRepo;
import com.coreConnect.coreConnect.services.EmailService;
import com.coreConnect.coreConnect.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
private UserRepo userRepo;
@Autowired
private PasswordEncoder passwordEncoder;
@Autowired
private EmailService emailService;
private Logger logger = Logger.getLogger(getClass().getName()); 
    @Override
    public User saveUser(User user) {
        String userID = UUID.randomUUID().toString();
        user.setUserId(userID);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("User saved successfully");
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        String emailtoken = UUID.randomUUID().toString();
        user.setEmailToken(emailtoken);
        User savedUser= userRepo.save(user);
    String emailLink = Helper.getLinkForEmailVerfication(emailtoken);
    emailService.sendEmail(savedUser.getEmail(),"verify Account", emailLink);
    return savedUser;

    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
      User user2 =userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
      user2.setName(user.getName());
      user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
user2.setPhoneNumber(user.getPhoneNumber());
user2.setProvider(user.getProvider());
user2.setProviderUserId(user.getProviderUserId());
User save = userRepo.save(user2);
return Optional.ofNullable(save);
    }

    @Override
    public void deleteUserById(String id) {
        User user2 =userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
  userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String id) {
        User user2 =userRepo.findById(id).orElse(null);
        if(user2 == null) {
            return false;
        }
        return true;
    }
    @Override
    public boolean isUserExistByEmail(String email) {
      User user2= userRepo.findByEmail(email).orElse(null); 
       return user2 != null ? true : false;   
    }

    @Override
    public User fetchUserByEmail(String email) {
        User user2 = userRepo.findByEmail(email).orElse(null);
        return user2;
    }

    @Override
    public User fetchUserByEmailAndPassword(String email, String password) {
     User user2 =  userRepo.findByEmailAndPassword(email,password).orElse(null);
     return user2;
    }

    @Override
    public List<User> fetchAllUsers() {
      return userRepo.findAll();
    }

    @Override
    public String fetchEmailByUserName(String name) {
        
        return userRepo.findEmailByName(name).orElse(null);
    }
    

}
