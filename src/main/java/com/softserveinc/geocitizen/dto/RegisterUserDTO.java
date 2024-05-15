/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.dto;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.softserveinc.geocitizen.entity.User.*;
import static com.softserveinc.tools.model.JsonError.Error.BAD_FIELD_FORMAT_NAME;
import static com.softserveinc.tools.model.JsonError.Error.MISSING_FIELD_NAME;

public class RegisterUserDTO implements Serializable {

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = LOGIN_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_LOGIN_LENGTH, max = MAX_LOGIN_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String login;

	@NotBlank(message = MISSING_FIELD_NAME)
	@Email(message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_EMAIL_LENGTH, max = MAX_EMAIL_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String email;

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = PASS_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String password;

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = NAME_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String name;

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = NAME_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_SURNAME_LENGTH, max = MAX_SURNAME_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String surname;

	public String getLogin() {
		return StringUtils.trim(login);
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return StringUtils.trim(email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return StringUtils.trim(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return StringUtils.trim(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return StringUtils.trim(surname);
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
