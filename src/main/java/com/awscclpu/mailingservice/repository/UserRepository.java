package com.awscclpu.mailingservice.repository;

import com.awscclpu.mailingservice.constant.MailingType;
import com.awscclpu.mailingservice.constant.Role;
import com.awscclpu.mailingservice.jooq.tables.records.UsersRecord;
import com.awscclpu.mailingservice.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.awscclpu.mailingservice.jooq.Tables.USERS;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	private final DSLContext dsl;

	public UsersRecord findByEmailAndUsername(String email, String username) {
		return dsl.selectFrom(USERS).where(USERS.EMAIL.eq(email)).and(USERS.USERNAME.eq(username)).fetchOne();
	}

	/**
	 * Saves a new user
	 *
	 * @param userDTO user to be saved
	 * @return Saved user
	 */
	public UsersRecord save(UserDTO userDTO) {
		return dsl.insertInto(USERS, USERS.ACTIVE, USERS.EMAIL, USERS.MAILING_TYPE, USERS.NAME, USERS.PASSWORD, USERS.ROLE, USERS.SENT_EMAILS, USERS.USERNAME)
				.values(Boolean.FALSE, userDTO.getEmail(), MailingType.BCC, userDTO.getName(), userDTO.getPassword(), Role.USER, 0L, userDTO.getUsername())
				.returning().fetchOne();
	}

	public boolean setActive(UserDTO userDTO, boolean activeFlag) {
		 return dsl.update(USERS).set(USERS.ACTIVE, activeFlag)
				.where(USERS.EMAIL.eq(userDTO.getEmail())).and(USERS.USERNAME.eq(userDTO.getUsername()))
				.returningResult(USERS.ACTIVE).fetchOne()
				.get(USERS.ACTIVE);
	}
}