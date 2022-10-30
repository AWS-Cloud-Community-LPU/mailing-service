package com.awscclpu.mailingservice.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.awscclpu.mailingservice.constant.PropertyConstants;
import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

	private final AmazonS3 s3Client;
	private final CacheService cacheService;
	private final Utilities utilities;
	private final ObjectMapper objectMapper;
	@Value("${" + PropertyConstants.AMAZON_S3_BUCKET + "}")
	private String bucketName;

	public S3ServiceImpl(AmazonS3 s3Client, CacheService cacheService, Utilities utilities, ObjectMapper objectMapper) {
		this.s3Client = s3Client;
		this.cacheService = cacheService;
		this.utilities = utilities;
		this.objectMapper = objectMapper;
	}

	@Override
	public APIInfo uploadTemplate(MultipartFile templateFile) {
		if (utilities.verifyTemplate(Objects.requireNonNull(templateFile.getOriginalFilename()))) {
			ObjectMetadata data = new ObjectMetadata();
			data.setContentType(templateFile.getContentType());
			data.setContentLength(templateFile.getSize());
			try {
				List<S3ObjectSummary> s3ObjectSummaries = cacheService.templatesList();
				s3ObjectSummaries.forEach((s3ObjectSummary) -> {
					if (s3ObjectSummary.getKey().equals(templateFile.getOriginalFilename()) && s3ObjectSummary.getBucketName().equals(bucketName)) {
						throw new RuntimeException("File Already exist in bucket: " + bucketName);
					}
				});
				s3Client.putObject(bucketName, templateFile.getOriginalFilename(), templateFile.getInputStream(), data);
			} catch (SdkClientException | IOException e) {
				log.error(e.getMessage());
				return new APIInfo(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		} else {
			return new APIInfo(HttpStatus.BAD_REQUEST, "Template Validation Failed");
		}
		cacheService.evictTemplatesCache();
		return new APIInfo(HttpStatus.OK, "File Uploaded Successfully");
	}

	@Override
	public APIInfo listTemplates() throws SdkClientException, JsonProcessingException {
		return new APIInfo(HttpStatus.OK, objectMapper.writeValueAsString(cacheService.templatesList()));
	}

	private S3Object viewTemplate(String templateName) {
		if (!utilities.verifyTemplate(templateName)) return null;
		return cacheService.templatesList().stream()
				.filter(s3ObjectSummary -> s3ObjectSummary.getKey().equals(templateName)).findFirst()
				.map(s3ObjectSummary -> s3Client.getObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey()))
				.orElse(null);
	}

	@Override
	public ByteArrayOutputStream downloadTemplate(String templateName) throws APIError {
		S3Object requiredTemplate = viewTemplate(templateName);
		if (requiredTemplate == null) throw new APIError(HttpStatus.BAD_REQUEST, "No Template Found");
		return downloadFile(requiredTemplate);
	}

	@Override
	public APIInfo deleteTemplate(String templateName) throws APIError, JsonProcessingException {
		if (!utilities.verifyTemplate(templateName))
			throw new APIError(HttpStatus.BAD_REQUEST, "Template Validation Failed");
		for (S3ObjectSummary s3ObjectSummary : cacheService.templatesList()) {
			if (s3ObjectSummary.getKey().equals(templateName)) {
				s3Client.deleteObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey());
			}
		}
		cacheService.evictTemplatesCache();
		return new APIInfo(HttpStatus.OK, objectMapper.writeValueAsString(cacheService.templatesList()));
	}

	private ByteArrayOutputStream downloadFile(S3Object s3Object) {
		InputStream inputStream = s3Object.getObjectContent();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[4096];
		try {
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
				outputStream.write(buffer, 0, len);
		} catch (IOException ex) {
			log.error("Exception Occurred while reading file", ex);
			throw new RuntimeException(ex);
		}
		return outputStream;
	}
}
