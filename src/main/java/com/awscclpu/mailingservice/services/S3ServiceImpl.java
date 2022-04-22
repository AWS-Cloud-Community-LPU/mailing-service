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
import java.util.ArrayList;
import java.util.List;

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
		ObjectMetadata data = new ObjectMetadata();
		data.setContentType(templateFile.getContentType());
		data.setContentLength(templateFile.getSize());
		if (templateFile.getOriginalFilename().endsWith(".template")) {
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
		List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
		for (Bucket b : buckets)
			for (S3ObjectSummary s3ObjectSummary : s3Client.listObjects(b.getName()).getObjectSummaries())
				if (s3ObjectSummary.getKey().endsWith(".template")) s3ObjectSummaries.add(s3ObjectSummary);
		return s3ObjectSummaries;
	}

	@Override
	public S3Object viewTemplate(String templateName) {
		for (S3ObjectSummary s3ObjectSummary : listTemplates())
			if (s3ObjectSummary.getKey().equals(templateName))
				return s3Client.getObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey());
		return null;
	}

	@Override
	public String deleteTemplate(String templateName) {
		for (S3ObjectSummary s3ObjectSummary : listTemplates())
			if (s3ObjectSummary.getKey().equals(templateName)) {
				s3Client.deleteObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey());
				return "Done";
			}
		return null;
	}
}
