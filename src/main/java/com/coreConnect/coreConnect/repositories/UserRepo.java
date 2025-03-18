package com.coreConnect.coreConnect.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coreConnect.coreConnect.entites.User;
import java.util.List;


public interface UserRepo extends JpaRepository<User, String> {
Optional<User> findByEmail(String email);
Optional<User> findByEmailAndPassword(String email, String password);
@Query(value = "SELECT user_email FROM users WHERE user_name = :name", nativeQuery = true)
Optional<String> findEmailByName(@Param("name") String name);
Optional<User> findByEmailToken(String emailToken);


}
