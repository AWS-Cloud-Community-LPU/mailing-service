package com.awscclpu.mailingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.DecimalFormat;

@Service
@Slf4j
public class OTPServiceImpl implements OTPService {
	private final CacheManager cacheManager;

	public OTPServiceImpl(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
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
}
