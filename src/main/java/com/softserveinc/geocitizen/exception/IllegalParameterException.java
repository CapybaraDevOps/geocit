package com.softserveinc.geocitizen.exception;

import com.softserveinc.tools.model.JsonError;

import static com.softserveinc.tools.model.JsonError.Error.ILLEGAL_PARAMETER;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/3/18 at 3:12 PM
 */
public class IllegalParameterException extends AbstractCitizenException {

	private final String field;

	public IllegalParameterException(String field) {
		this.field = field;
	}

	public IllegalParameterException(String field, String message) {
		super(message);

		this.field = field;
	}

	@Override
	public JsonError.Error getError() {
		return ILLEGAL_PARAMETER.forField(field);
	}
}
