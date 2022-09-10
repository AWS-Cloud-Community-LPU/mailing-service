package com.awscclpu.mailingservice.controllers;

import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.service.S3Service;
import com.awscclpu.mailingservice.service.Utilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/template")
public class TemplateController {

	private final Utilities utilities;
	private final S3Service s3Service;

	public TemplateController(S3Service s3Service, Utilities utilities) {
		this.s3Service = s3Service;
		this.utilities = utilities;
	}

	@PostMapping("/upload")
	public ResponseEntity<APIInfo> uploadObject(@RequestParam("file") MultipartFile templateFile) {
		APIInfo apiInfo = s3Service.uploadTemplate(templateFile);
		return ResponseEntity.status(apiInfo.getStatus()).body(apiInfo);
	}

	@GetMapping("/list")
	public ResponseEntity<APIInfo> listTemplates() throws JsonProcessingException {
		APIInfo apiInfo = s3Service.listTemplates();
		return ResponseEntity.status(apiInfo.getStatus()).body(apiInfo);
	}

	@GetMapping("/download")
	public ResponseEntity<byte[]> viewTemplate(@RequestHeader String templateName) throws APIError {
		return ResponseEntity.ok()
				.contentType(utilities.contentType(templateName))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + templateName + "\"")
				.body(s3Service.downloadTemplate(templateName).toByteArray());
	}

	@DeleteMapping("/delete")
	public ResponseEntity<APIInfo> deleteTemplate(@RequestHeader String templateName) throws APIError, JsonProcessingException {
		APIInfo apiInfo = s3Service.deleteTemplate(templateName);
		return ResponseEntity.status(apiInfo.getStatus()).body(apiInfo);
	}
}
