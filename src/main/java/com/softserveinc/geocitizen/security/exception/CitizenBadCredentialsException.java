package com.softserveinc.geocitizen.security.exception;

import com.softserveinc.geocitizen.security.exception.interfaces.ICitizenAuthenticationException;
import com.softserveinc.tools.model.JsonError;
import org.springframework.security.authentication.BadCredentialsException;

import static com.softserveinc.geocitizen.entity.User.MAX_FAILED_AUTH_VALUE;
import static com.softserveinc.tools.model.JsonError.Error.BAD_CREDENTIALS;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/4/18 at 5:01 PM
 */
public class CitizenBadCredentialsException extends BadCredentialsException implements ICitizenAuthenticationException {

	private final int failedAttempts;

	/**
	 * Constructs a <code>BadCredentialsException</code> with the specified message.
	 *
	 * @param msg
	 * 		the detail message
	 */
	public CitizenBadCredentialsException(String msg) {
		super(msg);

		this.failedAttempts = 0;
	}

	/**
	 * Constructs a <code>BadCredentialsException</code> with the specified message and failed attempts count.
	 *
	 * @param msg
	 * 		the detail message
	 */
	public CitizenBadCredentialsException(String msg, int failedAttempts) {
		super(msg);

		this.failedAttempts = failedAttempts;
	}

	/**
	 * Constructs a <code>BadCredentialsException</code> with the specified message and
	 * root cause.
	 *
	 * @param msg
	 * 		the detail message
	 * @param t
	 * 		root cause
	 */
	public CitizenBadCredentialsException(String msg, int failedAttempts, Throwable t) {
		super(msg, t);

		this.failedAttempts = failedAttempts;
	}

	@Override
	public JsonError.Error getError() {
		return failedAttempts == 0 ?
				BAD_CREDENTIALS :
				BAD_CREDENTIALS.forField(getFailedAttempts() + "/" + MAX_FAILED_AUTH_VALUE);
	}

	public int getFailedAttempts() {
		return failedAttempts;
	}
}
