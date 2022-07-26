package com.awscclpu.mailingservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.SecureRandom;
import java.text.DecimalFormat;

@Entity
@Getter
@Setter
@Table(name = "otp")
public final class OneTimePassword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean active;

	private int otp;

	public OneTimePassword() {
		super();
		this.active = true;
		this.otp = generateOTP();
	}

	private int generateOTP() {
		String otp = new DecimalFormat("000000").format(new SecureRandom().nextInt(999999));
		return Integer.parseInt(otp);
	}
}
