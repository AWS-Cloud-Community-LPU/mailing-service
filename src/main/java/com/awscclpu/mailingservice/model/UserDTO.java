package com.awscclpu.mailingservice.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDTO {
	@NotNull
	String name;
	@NotNull
	String username;
	@NotNull
	String email;
}
