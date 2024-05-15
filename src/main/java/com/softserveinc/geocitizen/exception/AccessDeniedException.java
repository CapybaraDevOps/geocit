package com.softserveinc.geocitizen.exception;

import com.softserveinc.tools.model.JsonError;

public class AccessDeniedException extends AbstractCitizenException {

	@Override
	public JsonError.Error getError() {
		return JsonError.Error.ACCESS_DENIED;
	}
}
