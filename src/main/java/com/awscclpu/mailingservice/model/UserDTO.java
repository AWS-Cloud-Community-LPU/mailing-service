package com.awscclpu.mailingservice.model;

import com.awscclpu.mailingservice.jooq.tables.records.UsersRecord;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NotNull
@Getter
@Setter
@RequiredArgsConstructor
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


	public boolean equals(UsersRecord usersRecord) {
		if(usersRecord == null) return false;
		return (usersRecord.getUsername().equals(this.getUsername())
				&& usersRecord.getEmail().equals(this.getEmail())
				&& usersRecord.getName().equals(this.getName()));
	}
}
