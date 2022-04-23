package com.awscclpu.mailingservice.controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.awscclpu.mailingservice.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/template")
public class TemplateController {

	private final S3Service s3Service;

	@Autowired
	public TemplateController(S3Service s3Service) {
		this.s3Service = s3Service;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadObject(@RequestParam String bucketName, @RequestParam("file") MultipartFile templateFile) {
		return ResponseEntity.ok(s3Service.uploadTemplate(bucketName, templateFile));
	}

	@GetMapping("/list")
	public ResponseEntity<List<S3ObjectSummary>> listTemplates() {
		return ResponseEntity.ok(s3Service.listTemplates());
	}

	@GetMapping("/view")
	public ResponseEntity<S3Object> viewBucket(@RequestHeader String templateName) {
		return ResponseEntity.ok(s3Service.viewTemplate(templateName));
	}

	@PostMapping("/delete")
	public ResponseEntity<String> deleteBucket(@RequestHeader String templateName) {
		return ResponseEntity.ok(s3Service.deleteTemplate(templateName));
	}
}
