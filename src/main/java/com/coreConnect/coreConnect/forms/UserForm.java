package com.coreConnect.coreConnect.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
@NotBlank(message = "Name is required")
@Size(min = 3, message = "Minimum 3 characters required")
private String name;

@Email(message = "Email should be valid")
@NotBlank(message = "Email is required")
private String email;
@Size(min = 6, message = "Minimum 6 characters required")
@NotBlank(message = "Password is required")
private String password;
@NotBlank(message= "About is required")
private String about;
@NotBlank(message = "Phone number is required")
@Size(min = 8, max = 12, message = "Phone number should be of 10 digits")
private String phoneNumber;

private MultipartFile userImage;
private String profilePic;
}
