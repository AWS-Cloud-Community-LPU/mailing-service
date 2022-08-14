package com.awscclpu.mailingservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {
	@NotNull
	String name;
	@NotNull
	String username;
	@NotNull
	String email;
}
