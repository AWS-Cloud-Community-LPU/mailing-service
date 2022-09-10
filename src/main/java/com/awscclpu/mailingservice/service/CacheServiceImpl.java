package com.awscclpu.mailingservice.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.awscclpu.mailingservice.constant.PropertyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {
	private final CacheManager cacheManager;
	private final AmazonS3 s3Client;
	private final Utilities utilities;
	@Value("${" + PropertyConstants.AMAZON_S3_BUCKET + "}")
	private String bucketName;

	public CacheServiceImpl(CacheManager cacheManager, AmazonS3 s3Client, Utilities utilities) {
		this.cacheManager = cacheManager;
		this.s3Client = s3Client;
		this.utilities = utilities;
	}

	@Cacheable(value = "otpCache")
	@Override
	public Integer getOTP(String username) {
		log.info("Trying to get OTP for: " + username);
		return Integer.parseInt(cacheManager.getCache("otpCache").get("username").toString());
	}

	@Cacheable(value = "otpCache")
	@Override
	public Integer generateOTP(String username) {
		log.info("Trying to generate OTP for: " + username);
		return Integer.parseInt(new DecimalFormat("000000").format(new SecureRandom().nextInt(999999)));
	}

	@Cacheable(value = "templatesCache")
	@Override
	public List<S3ObjectSummary> templatesList() throws SdkClientException {
		log.info("Populating Templates Cache");
		ObjectListing bucketObjects = s3Client.listObjects(bucketName);
		return bucketObjects.getObjectSummaries().stream()
				.filter(s3ObjectSummary -> utilities.verifyTemplate(s3ObjectSummary.getKey()))
				.collect(Collectors.toList());
	}

	@CacheEvict(value = "templatesCache")
	@Override
	public void evictTemplatesCache() {
		log.info("Evicted Templates Cache");
	}

	@CacheEvict(value = "otpCache")
	@Override
	public void evictOTPCache() {
		log.info("Evicted OTP Cache");
	}

}
