package com.softserveinc.geocitizen.security.exception;

import com.softserveinc.geocitizen.security.exception.interfaces.ICitizenAuthenticationException;
import com.softserveinc.tools.model.JsonError;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/5/18 at 1:06 PM
 */
public class LoginNotFoundException extends UsernameNotFoundException implements ICitizenAuthenticationException {

	public LoginNotFoundException(String msg) {
		super(msg);
	}

	public LoginNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	@Override
	public JsonError.Error getError() {
		return JsonError.Error.USER_NOT_EXIST.forField("login");
	}
}
