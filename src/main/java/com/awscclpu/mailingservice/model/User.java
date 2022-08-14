package com.awscclpu.mailingservice.model;

import com.awscclpu.mailingservice.constant.Constants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(columnDefinition = "VARCHAR(150)")
	private String name;
	@Column(unique = true, columnDefinition = "VARCHAR(40)")
	@Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "USERNAME NOT VALID")
	private String username;
	@Column(unique = true, length = 512)
	@Email(message = "EMAIL NOT VALID")
	@NotNull(message = "EMAIL CANNOT BE EMPTY")
	private String email;
	private boolean active;
	private Long sent_emails;
	private Constants.SendType mailingType;
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
	}

	public User(UserDTO user) {
		this(user.getName(), user.getUsername(), user.getEmail());
	}
}
