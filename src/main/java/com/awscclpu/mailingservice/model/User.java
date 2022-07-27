package com.awscclpu.mailingservice.model;

import com.awscclpu.mailingservice.constants.EmailConstants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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

	@Column(columnDefinition = "VARCHAR(40)")
	private String name;

	@Column(unique = true)
	private String email;

	private boolean active;

	private Long sent_emails;

	private EmailConstants.SendType mailingType;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	OneTimePassword oneTimePassword;

	public User() {
		super();
	}

	public User(String name, String email) {
		super();
		this.name = name;
		this.email = email;
		this.active = false;
		this.sent_emails = 0L;
		this.oneTimePassword = new OneTimePassword();
		this.mailingType = EmailConstants.SendType.BCC;
	}
}
