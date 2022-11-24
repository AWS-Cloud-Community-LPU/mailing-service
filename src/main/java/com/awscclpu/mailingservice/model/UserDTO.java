package com.awscclpu.mailingservice.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NotNull
public class UserDTO {
	@NotNull
	String name;

	@NotNull
	@Pattern(regexp = "^[\\w](?!.*?\\.{2})[\\w.]{1,28}[\\w]$", message = "USERNAME NOT VALID")
	String username;

	// Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:
	@NotNull
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "PASSWORD NOT VALID")
	String password;

	@NotNull
	@Email
	String email;
}
