package com.coreConnect.coreConnect.forms;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {
  private String contactId;
      private String contactName;
       private String contactEmail;
      private String contactPhone;
    private String address;
      private String description;
    private boolean favrouite;
    private String websiteLink;
    private String linkedInLink;

    private MultipartFile contactImage;
    private String picture;
}
