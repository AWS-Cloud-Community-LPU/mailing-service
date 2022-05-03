package com.awscclpu.mailingservice.services;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

	String uploadTemplate(String bucketName, MultipartFile file);

	List<S3ObjectSummary> listTemplates();

	String deleteTemplate(String templateName);

	S3Object viewTemplate(String templateName);

}
