/*
 * This file is generated by jOOQ.
 */
package com.awscclpu.mailingservice.jooq.tables.records;


import com.awscclpu.mailingservice.constant.MailingType;
import com.awscclpu.mailingservice.constant.Role;
import com.awscclpu.mailingservice.jooq.tables.Users;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersRecord extends UpdatableRecordImpl<UsersRecord> implements Record11<Long, Boolean, String, LocalDateTime, MailingType, String, String, Role, Long, LocalDateTime, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.users.id</code>.
     */
    public UsersRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.users.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.users.active</code>.
     */
    public UsersRecord setActive(Boolean value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.users.active</code>.
     */
    public Boolean getActive() {
        return (Boolean) get(1);
    }

    /**
     * Setter for <code>public.users.email</code>.
     */
    public UsersRecord setEmail(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.users.email</code>.
     */
    public String getEmail() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.users.generated_at</code>.
     */
    public UsersRecord setGeneratedAt(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.users.generated_at</code>.
     */
    public LocalDateTime getGeneratedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>public.users.mailing_type</code>.
     */
    public UsersRecord setMailingType(MailingType value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.users.mailing_type</code>.
     */
    public MailingType getMailingType() {
        return (MailingType) get(4);
    }

    /**
     * Setter for <code>public.users.name</code>.
     */
    public UsersRecord setName(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.users.name</code>.
     */
    public String getName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.users.password</code>.
     */
    public UsersRecord setPassword(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.users.password</code>.
     */
    public String getPassword() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.users.role</code>.
     */
    public UsersRecord setRole(Role value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.users.role</code>.
     */
    public Role getRole() {
        return (Role) get(7);
    }

    /**
     * Setter for <code>public.users.sent_emails</code>.
     */
    public UsersRecord setSentEmails(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.users.sent_emails</code>.
     */
    public Long getSentEmails() {
        return (Long) get(8);
    }

    /**
     * Setter for <code>public.users.updated_at</code>.
     */
    public UsersRecord setUpdatedAt(LocalDateTime value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>public.users.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(9);
    }

    /**
     * Setter for <code>public.users.username</code>.
     */
    public UsersRecord setUsername(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>public.users.username</code>.
     */
    public String getUsername() {
        return (String) get(10);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record11 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row11<Long, Boolean, String, LocalDateTime, MailingType, String, String, Role, Long, LocalDateTime, String> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    @Override
    public Row11<Long, Boolean, String, LocalDateTime, MailingType, String, String, Role, Long, LocalDateTime, String> valuesRow() {
        return (Row11) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Users.USERS.ID;
    }

    @Override
    public Field<Boolean> field2() {
        return Users.USERS.ACTIVE;
    }

    @Override
    public Field<String> field3() {
        return Users.USERS.EMAIL;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Users.USERS.GENERATED_AT;
    }

    @Override
    public Field<MailingType> field5() {
        return Users.USERS.MAILING_TYPE;
    }

    @Override
    public Field<String> field6() {
        return Users.USERS.NAME;
    }

    @Override
    public Field<String> field7() {
        return Users.USERS.PASSWORD;
    }

    @Override
    public Field<Role> field8() {
        return Users.USERS.ROLE;
    }

    @Override
    public Field<Long> field9() {
        return Users.USERS.SENT_EMAILS;
    }

    @Override
    public Field<LocalDateTime> field10() {
        return Users.USERS.UPDATED_AT;
    }

    @Override
    public Field<String> field11() {
        return Users.USERS.USERNAME;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Boolean component2() {
        return getActive();
    }

    @Override
    public String component3() {
        return getEmail();
    }

    @Override
    public LocalDateTime component4() {
        return getGeneratedAt();
    }

    @Override
    public MailingType component5() {
        return getMailingType();
    }

    @Override
    public String component6() {
        return getName();
    }

    @Override
    public String component7() {
        return getPassword();
    }

    @Override
    public Role component8() {
        return getRole();
    }

    @Override
    public Long component9() {
        return getSentEmails();
    }

    @Override
    public LocalDateTime component10() {
        return getUpdatedAt();
    }

    @Override
    public String component11() {
        return getUsername();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Boolean value2() {
        return getActive();
    }

    @Override
    public String value3() {
        return getEmail();
    }

    @Override
    public LocalDateTime value4() {
        return getGeneratedAt();
    }

    @Override
    public MailingType value5() {
        return getMailingType();
    }

    @Override
    public String value6() {
        return getName();
    }

    @Override
    public String value7() {
        return getPassword();
    }

    @Override
    public Role value8() {
        return getRole();
    }

    @Override
    public Long value9() {
        return getSentEmails();
    }

    @Override
    public LocalDateTime value10() {
        return getUpdatedAt();
    }

    @Override
    public String value11() {
        return getUsername();
    }

    @Override
    public UsersRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public UsersRecord value2(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public UsersRecord value3(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public UsersRecord value4(LocalDateTime value) {
        setGeneratedAt(value);
        return this;
    }

    @Override
    public UsersRecord value5(MailingType value) {
        setMailingType(value);
        return this;
    }

    @Override
    public UsersRecord value6(String value) {
        setName(value);
        return this;
    }

    @Override
    public UsersRecord value7(String value) {
        setPassword(value);
        return this;
    }

    @Override
    public UsersRecord value8(Role value) {
        setRole(value);
        return this;
    }

    @Override
    public UsersRecord value9(Long value) {
        setSentEmails(value);
        return this;
    }

    @Override
    public UsersRecord value10(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public UsersRecord value11(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public UsersRecord values(Long value1, Boolean value2, String value3, LocalDateTime value4, MailingType value5, String value6, String value7, Role value8, Long value9, LocalDateTime value10, String value11) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UsersRecord
     */
    public UsersRecord() {
        super(Users.USERS);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(Long id, Boolean active, String email, LocalDateTime generatedAt, MailingType mailingType, String name, String password, Role role, Long sentEmails, LocalDateTime updatedAt, String username) {
        super(Users.USERS);

        setId(id);
        setActive(active);
        setEmail(email);
        setGeneratedAt(generatedAt);
        setMailingType(mailingType);
        setName(name);
        setPassword(password);
        setRole(role);
        setSentEmails(sentEmails);
        setUpdatedAt(updatedAt);
        setUsername(username);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(com.awscclpu.mailingservice.jooq.tables.pojos.Users value) {
        super(Users.USERS);

        if (value != null) {
            setId(value.getId());
            setActive(value.getActive());
            setEmail(value.getEmail());
            setGeneratedAt(value.getGeneratedAt());
            setMailingType(value.getMailingType());
            setName(value.getName());
            setPassword(value.getPassword());
            setRole(value.getRole());
            setSentEmails(value.getSentEmails());
            setUpdatedAt(value.getUpdatedAt());
            setUsername(value.getUsername());
        }
    }
}
