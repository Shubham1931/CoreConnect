package com.coreConnect.coreConnect.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coreConnect.coreConnect.entites.Contact;
import com.coreConnect.coreConnect.entites.User;
@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {

// find the contacts by user;
Page<Contact> findByUser(User user,Pageable pageable);
@Query("SELECT c FROM Contact c WHERE c.user.id = :userID")
List<Contact> findByUserId(@Param("userID")String userId);
Page<Contact> findByUserAndContactNameContaining(User user ,String contactName,Pageable pageable);
Page<Contact> findByUserAndContactEmailContaining(User user ,String contactEmail,Pageable pageable);
Page<Contact> findByUserAndContactPhoneContaining(User user ,String contactPhone,Pageable pageable);
@Query("SELECT COUNT(c) FROM Contact c WHERE c.user.email = :email")
long countContactsByUserEmail(@Param("email") String email);
}
