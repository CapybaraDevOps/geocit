package com.softserveinc.geocitizen.dto;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.softserveinc.geocitizen.entity.User.*;
import static com.softserveinc.tools.model.JsonError.Error.BAD_FIELD_FORMAT_NAME;
import static com.softserveinc.tools.model.JsonError.Error.MISSING_FIELD_NAME;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/24/18 at 2:16 PM
 */
public class EditUserDTO implements Serializable {

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = NAME_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String name;

	@NotBlank(message = MISSING_FIELD_NAME)
	@Pattern(regexp = NAME_PATTERN, message = BAD_FIELD_FORMAT_NAME)
	@Size(min = MIN_SURNAME_LENGTH, max = MAX_SURNAME_LENGTH, message = BAD_FIELD_FORMAT_NAME)
	private String surname;

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
