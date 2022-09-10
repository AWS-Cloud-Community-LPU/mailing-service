package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface S3Service {

	APIInfo uploadTemplate(MultipartFile file);

	APIInfo listTemplates() throws JsonProcessingException;

	APIInfo deleteTemplate(String templateName) throws APIError, JsonProcessingException;

	ByteArrayOutputStream downloadTemplate(String templateName) throws APIError;

}
