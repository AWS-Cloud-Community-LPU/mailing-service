package com.awscclpu.mailingservice.model;

import com.awscclpu.mailingservice.exception.APIError;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NotNull(groups = APIError.class)
public class UserDTO {
	@NotNull(groups = APIError.class)
	String name;
	@NotNull(groups = APIError.class)
	@Pattern(regexp = "^[\\w](?!.*?\\.{2})[\\w.]{1,28}[\\w]$", message = "USERNAME NOT VALID")
	String username;
	@NotNull(groups = APIError.class)
	@Email(groups = APIError.class)
	String email;
}
