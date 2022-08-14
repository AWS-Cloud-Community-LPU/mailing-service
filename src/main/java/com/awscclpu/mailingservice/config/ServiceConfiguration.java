package com.awscclpu.mailingservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.awscclpu.mailingservice.constant.PropertyConstants;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class ServiceConfiguration {

	@Value("${" + PropertyConstants.AMAZON_S3_ACCESS_KEY + "}")
	private String amazonS3AccessKey;

	@Value("${" + PropertyConstants.AMAZON_S3_SECRET_KEY + "}")
	private String amazonS3SecretKey;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
		propsConfig.setLocation(new ClassPathResource("git.properties"));
		propsConfig.setIgnoreResourceNotFound(true);
		propsConfig.setIgnoreUnresolvablePlaceholders(true);
		return propsConfig;
	}

	@Bean
	public AmazonS3 getS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(amazonS3AccessKey, amazonS3SecretKey);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
	}

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("otpCache");
		caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
				.expireAfterWrite(2, TimeUnit.MINUTES)
				.initialCapacity(200)
				.maximumSize(20000));
		return caffeineCacheManager;
	}
}
