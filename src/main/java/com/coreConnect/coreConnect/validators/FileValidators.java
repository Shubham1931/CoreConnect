package com.coreConnect.coreConnect.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidators implements ConstraintValidator<ValidFile,MultipartFile> {
public static final long MAX_FILE_SIZE = 1024+1024*2;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file==null|| file.isEmpty()){

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("file cannot be empty").addConstraintViolation();
            return false;
        }
        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File should be less than 2mb").addConstraintViolation();
            return false; 
        }
        return true;
    }

}
