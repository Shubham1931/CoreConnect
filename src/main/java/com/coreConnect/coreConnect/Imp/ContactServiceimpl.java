package com.coreConnect.coreConnect.Imp;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coreConnect.coreConnect.entites.Contact;
import com.coreConnect.coreConnect.entites.User;
import com.coreConnect.coreConnect.helpers.ResourceNotFoundException;
import com.coreConnect.coreConnect.repositories.ContactRepo;
import com.coreConnect.coreConnect.services.ContactService;
@Service
public class ContactServiceimpl implements ContactService{
@Autowired
private ContactRepo contactRepo;
    @Override
    public void delete(String id) {
      
        contactRepo.deleteById(id);
    }

    @Override
    public List<Contact> getAll() {
 
       return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
      
     return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public List<Contact> getByUserId(String userId) {
       return contactRepo.findByUserId(userId);
     
    }

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setContactId(contactId);
        return contactRepo.save(contact);
    }

      @Override
    public Contact update(Contact contact) {
             Contact contactOld = contactRepo.findById(contact.getContactId()).orElseThrow(()-> new RuntimeException("contactNotFound"));
             contactOld.setContactName(contact.getContactName());
             contactOld.setContactEmail(contact.getContactEmail());
             contactOld.setContactPhone(contact.getContactPhone());
             contactOld.setAddress(contact.getAddress());
             contactOld.setDescription(contact.getDescription());
             contactOld.setWebsiteLink(contact.getWebsiteLink());
             contactOld.setLinkedInLink(contact.getLinkedInLink());
             contactOld.setFavrouite(contact.isFavrouite());
             contactOld.setPicture(contact.getPicture());
             contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
             return contactRepo.save(contactOld);
             


    }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
      Sort sort = direction.equals("desc")?Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size,sort);
       return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByContactName(String contactName, int page, int size, String sortBy, String direction,
    User user) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size,sort);
       return contactRepo.findByUserAndContactNameContaining(user,contactName,pageable);
    }

    @Override
    public Page<Contact>  searchByContactEmail(String contactEmail, int page, int size, String sortBy, String direction ,User user) {
        Sort sort = direction.equals("desc")?Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size,sort);
        return contactRepo.findByUserAndContactEmailContaining(user,contactEmail,pageable);
    }

    @Override
    public Page<Contact>  searchByContactPhone(String contactPhone, int page, int size, String sortBy,
            String direction ,User user) {
                Sort sort = direction.equals("desc")?Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
                var pageable = PageRequest.of(page, size,sort);
                return contactRepo.findByUserAndContactPhoneContaining(user,contactPhone,pageable);
    }

    @Override
    public long getTotalContactsByUserEmail(String email) {
        // TODO Auto-generated method stub
       return contactRepo.countContactsByUserEmail(email);
    }

  

  
  

}
