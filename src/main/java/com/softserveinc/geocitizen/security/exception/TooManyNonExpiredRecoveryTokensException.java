package com.softserveinc.geocitizen.security.exception;

import com.softserveinc.geocitizen.security.exception.interfaces.ICitizenAuthenticationException;
import com.softserveinc.tools.model.JsonError;
import org.springframework.security.core.AuthenticationException;

import static com.softserveinc.tools.model.JsonError.Error.TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/4/18 at 1:40 PM
 */
public class TooManyNonExpiredRecoveryTokensException extends AuthenticationException
		implements ICitizenAuthenticationException {

	/**
	 * Constructs a <code>EmailNotFoundException</code> with the specified message.
	 *
	 * @param msg
	 * 		the detail message.
	 */
	public TooManyNonExpiredRecoveryTokensException(String msg) {
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
	public TooManyNonExpiredRecoveryTokensException(String msg, Throwable t) {
		super(msg, t);
	}

	@Override
	public JsonError.Error getError() {
		return TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS;
	}
}
