package com.softserveinc.geocitizen.security.exception;

import com.softserveinc.geocitizen.security.exception.interfaces.ICitizenAuthenticationException;
import com.softserveinc.tools.model.JsonError;
import org.springframework.security.core.AuthenticationException;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/4/18 at 3:36 PM
 */
public class EmailNotFoundException extends AuthenticationException implements ICitizenAuthenticationException {

	/**
	 * Constructs a <code>EmailNotFoundException</code> with the specified message.
	 *
	 * @param msg
	 * 		the detail message.
	 */
	public EmailNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a {@code EmailNotFoundException} with the specified message and root
	 * cause.
	 *
	 * @param msg
	 * 		the detail message.
	 * @param t
	 * 		root cause
	 */
	public EmailNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	@Override
	public JsonError.Error getError() {
		return JsonError.Error.USER_NOT_EXIST.forField("email");
	}
}
