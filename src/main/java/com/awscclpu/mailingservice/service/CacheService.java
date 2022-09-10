package com.awscclpu.mailingservice.service;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

public interface CacheService {
	Integer getOTP(String username);

	Integer generateOTP(String username);

	List<S3ObjectSummary> templatesList();

	void evictTemplatesCache();

	void evictOTPCache();
}
