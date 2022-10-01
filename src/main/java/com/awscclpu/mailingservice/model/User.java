package com.awscclpu.mailingservice.model;

import com.awscclpu.mailingservice.constant.Constants;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(columnDefinition = "VARCHAR(150)")
	private String name;
	@Column(unique = true, columnDefinition = "VARCHAR(30)")
	@Pattern(regexp = "^[\\w](?!.*?\\.{2})[\\w.]{1,28}[\\w]$", message = "USERNAME NOT VALID")
	private String username;
	@Column(unique = true, length = 512)
	@Email(message = "EMAIL NOT VALID")
	@NotNull(message = "EMAIL CANNOT BE EMPTY")
	private String email;
	private boolean active;
	private Long sent_emails;
	private Constants.SendType mailingType;
	private Constants.Role role;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	@CreationTimestamp
	private LocalDateTime generatedAt;

	public User() {
		super();
	}

	public User(String name, String username, String email) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.active = false;
		this.sent_emails = 0L;
		this.mailingType = Constants.SendType.BCC;
		this.role = Constants.Role.USER;
	}

	public User(UserDTO user) {
		this(user.getName(), user.getUsername(), user.getEmail());
	}

	public boolean equals(UserDTO userDTO) {
		return (userDTO.getUsername().equals(this.getUsername()) && userDTO.getEmail().equals(this.getEmail()) && userDTO.getName().equals(this.getName()));
	}
}
