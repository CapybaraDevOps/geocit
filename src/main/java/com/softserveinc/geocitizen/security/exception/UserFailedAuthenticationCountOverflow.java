package com.softserveinc.geocitizen.security.exception;

import com.softserveinc.geocitizen.security.exception.interfaces.ICitizenAuthenticationException;
import com.softserveinc.tools.model.JsonError;
import org.springframework.security.core.AuthenticationException;

import static com.softserveinc.tools.model.JsonError.Error.USER_BLOCKED_BY_MAX_FAILED_AUTH;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/4/18 at 4:36 PM
 */
public class UserFailedAuthenticationCountOverflow extends AuthenticationException
		implements ICitizenAuthenticationException {

	public UserFailedAuthenticationCountOverflow(String msg) {
		super(msg);
	}

	public UserFailedAuthenticationCountOverflow(String msg, Throwable t) {
		super(msg, t);
	}

	@Override
	public JsonError.Error getError() {
		return USER_BLOCKED_BY_MAX_FAILED_AUTH;
	}
}
