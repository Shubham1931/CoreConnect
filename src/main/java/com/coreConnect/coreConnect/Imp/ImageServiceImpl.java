package com.coreConnect.coreConnect.Imp;

import java.io.IOException;
import java.util.UUID;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.coreConnect.coreConnect.helpers.AppConstants;
import com.coreConnect.coreConnect.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String filename ) {
       
        try {
            // Upload directly using getBytes() to avoid manual byte handling
            Map uploadResult = cloudinary.uploader().upload(contactImage.getBytes(), ObjectUtils.asMap(
                "public_id", filename,  // Fix typo: "publicId" → "public_id"
                "resource_type", "image" // Ensure it's uploaded as an image
            ));

            // Get the secure URL directly from Cloudinary's response
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url()
                .transformation(new Transformation<>()
                    .width(AppConstants.CONTACT_IMAGE_WIDTH)
                    .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                    .crop(AppConstants.CONTACT_IMAGE_CROP))
                .generate(publicId);
    }
}
