package com.softserveinc.geocitizen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.softserveinc.geocitizen.entity.User.*;
import static com.softserveinc.tools.model.JsonError.Error.BAD_FIELD_FORMAT_NAME;
import static com.softserveinc.tools.model.JsonError.Error.MISSING_FIELD_NAME;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/22/18 at 2:53 PM
 */
public class SubmitRegistrationDTO {

	public static final String REGISTRATION_TOKEN_FIELD = "registration_token";

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = LOGIN_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_LOGIN_LENGTH, max = MAX_LOGIN_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String login;

	@NotBlank(message = MISSING_FIELD_NAME)
	@Size(max = MAX_REGISTRATION_TOKEN_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	@JsonProperty(REGISTRATION_TOKEN_FIELD)
	private String registrationToken;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getRegistrationToken() {
		return registrationToken;
	}

	public void setRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}
}
