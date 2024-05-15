package com.softserveinc.geocitizen.exception;

import com.softserveinc.tools.model.JsonError;

/**
 * Interface of exceptions that can give us some error
 *
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0 Created 12/24/17 at 2:19 AM
 * @see com.softserveinc.tools.model.JsonError.Error
 */
public abstract class AbstractCitizenException extends Exception {

	public AbstractCitizenException() {
		super();
	}

	public AbstractCitizenException(String message) {
		super(message);
	}

	public AbstractCitizenException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbstractCitizenException(Throwable cause) {
		super(cause);
	}

	protected AbstractCitizenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public abstract JsonError.Error getError();
}
