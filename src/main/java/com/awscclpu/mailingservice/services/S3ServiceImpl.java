package com.awscclpu.mailingservice.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

	private final AmazonS3 s3Client;

	@Autowired
	public S3ServiceImpl(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	@Override
	public String uploadTemplate(String bucketName, MultipartFile templateFile) {
		if (verifyTemplate(Objects.requireNonNull(templateFile.getOriginalFilename()))) {
			ObjectMetadata data = new ObjectMetadata();
			data.setContentType(templateFile.getContentType());
			data.setContentLength(templateFile.getSize());
			List<S3ObjectSummary> s3ObjectSummaries = listTemplates();
			s3ObjectSummaries.forEach((s3ObjectSummary) -> {
				if (s3ObjectSummary.getKey().equals(templateFile.getOriginalFilename())) throw new RuntimeException();
			});
			try {
				s3Client.putObject(bucketName, templateFile.getOriginalFilename(), templateFile.getInputStream(), data);
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@Override
	public List<S3ObjectSummary> listTemplates() {
		List<Bucket> buckets = s3Client.listBuckets();
		return buckets.stream()
				.flatMap(b -> s3Client.listObjects(b.getName()).getObjectSummaries().stream())
				.filter(s3ObjectSummary -> verifyTemplate(s3ObjectSummary.getKey()))
				.collect(Collectors.toList());
	}

	@Override
	public S3Object viewTemplate(String templateName) {
		if (!verifyTemplate(templateName)) return null;
		return listTemplates().stream()
				.filter(s3ObjectSummary -> s3ObjectSummary.getKey().equals(templateName)).findFirst()
				.map(s3ObjectSummary -> s3Client.getObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey()))
				.orElse(null);
	}

	@Override
	public String deleteTemplate(String templateName) {
		verifyTemplate(templateName);
		listTemplates().stream()
				.filter(s3ObjectSummary -> s3ObjectSummary.getKey().equals(templateName))
				.forEach(s3ObjectSummary -> s3Client.deleteObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey()));
		return null;
	}

	public boolean verifyTemplate(String templateName) {
		return templateName.endsWith(".template");
	}
}
