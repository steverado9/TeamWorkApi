package com.steverado.TeamWorkApi.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.steverado.TeamWorkApi.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        logger.info("Received request to upload file");

        try {
            final Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            final String url = (String) result.get("secure_url");
            logger.info("Returning URL from cloudinary '{}'", url);

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file" + e.getMessage(), e);
        }
    }
}
