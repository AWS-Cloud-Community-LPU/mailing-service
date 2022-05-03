package com.awscclpu.mailingservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.awscclpu.mailingservice.constants.PropertyConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

	@Value("${" + PropertyConstants.AMAZON_S3_ACCESS_KEY + "}")
	private String amazonS3AccessKey;

	@Value("${" + PropertyConstants.AMAZON_S3_SECRET_KEY + "}")
	private String amazonS3SecretKey;

	@Bean
	public AmazonS3 getS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(amazonS3AccessKey, amazonS3SecretKey);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
		return s3client;
	}
}
