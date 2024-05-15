/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.tools.model;

import java.util.ArrayList;
import java.util.EnumSet;

public class SystemDAOException extends Exception {

	private Exception exception;

	public SystemDAOException() {
		super();
	}

	public SystemDAOException(Exception exception) {
		this.exception = exception;
	}

	public SystemDAOException(String message) {
		super(message);
	}

	public SystemDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemDAOException(Throwable cause) {
		super(cause);
	}

	public SystemDAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String getMessage() {
		return exception != null ? exception.getMessage() : super.getMessage();
	}

	public Exception getException() {
		return exception;
	}

	public enum Exception {
		NOT_FOUND(0, "NOT FOUND"),
		ALREADY(1, "ALREADY EXISTS"),
		MYSQL(2, "MySQL ERROR"),;

		private static final ArrayList<Exception> lookup = new ArrayList<>();

		static {
			EnumSet.allOf(Exception.class).forEach(e -> lookup.add(e.getId(), e));
		}

		private final int id;
		private final String message;

		Exception(int id, String message) {
			this.id = id;
			this.message = message;
		}

		public static Exception get(int id) {
			return lookup.get(id);
		}

		public int getId() {
			return id;
		}

		public String getMessage() {
			return message;
		}
	}
}
