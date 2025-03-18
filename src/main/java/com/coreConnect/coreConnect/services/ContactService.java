package com.coreConnect.coreConnect.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.coreConnect.coreConnect.entites.Contact;
import com.coreConnect.coreConnect.entites.User;
@Service
public interface ContactService {
Contact save(Contact contact);

Contact update(Contact contact);
List<Contact> getAll();
Contact getById(String id);
void delete(String id);
Page<Contact>  searchByContactName(String contactName,int page,int size,String sortField,String sortDirection,User user);
Page<Contact>  searchByContactEmail(String contactEmail,int page,int size,String sortField,String sortDirection,User user);
Page<Contact>  searchByContactPhone(String contactPhone,int page,int size,String sortField,String sortDirection,User user);
List<Contact> getByUserId(String userId);
Page<Contact> getByUser(User user,int page,int size,String sortField,String sortDirection);
public long getTotalContactsByUserEmail(String email);
}
// 