package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.exception.APIError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPServiceImpl implements OTPService {

	private final JavaMailSender mailSender;

	/**
	 * This Method sends OTP to the email provided
	 *
	 * @param username Username of user
	 * @param email    Email of user
	 * @param otp      Generated OTP
	 * @throws APIError When Mail Exception is thrown from mail sender
	 * @implNote Exception thrown needs not to be caught in calling classes as database roll back needs to happen (Use @Transactional in Calling class for roll back)
	 */
	@Override
	public void sendOTP(String username, String email, int otp) throws APIError {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject("Hey " + username + ", We Got your OTP Right Here");
		msg.setText("Hi " + username + ",\n\nGenerated OTP: " + otp + "\n\nWarm Regards,\nAWS Cloud Community LPU");
		try {
			mailSender.send(msg);
		} catch (MailException ex) {
			log.error(ex.getMessage(), ex);
			throw new APIError(HttpStatus.INTERNAL_SERVER_ERROR, "Oops!! We are facing issues with our Email Vendor, We will get back as soon as possible");
		}
	}
}
