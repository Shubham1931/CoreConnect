package com.coreConnect.coreConnect.services;

import java.util.List;
import java.util.Optional;

import com.coreConnect.coreConnect.entites.User;

public interface UserService {
User saveUser(User user);
Optional<User> getUserById(String id);
Optional<User> updateUser(User user);
void deleteUserById(String id);
boolean isUserExist(String id);
boolean isUserExistByEmail(String email);
User fetchUserByEmail(String email);
User fetchUserByEmailAndPassword(String email, String password);
String fetchEmailByUserName(String name);
List<User> fetchAllUsers();

}
