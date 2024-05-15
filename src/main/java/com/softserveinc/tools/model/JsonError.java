package com.softserveinc.tools.model;

import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0 Created 12/20/17 at 1:11 AM
 */
public class JsonError {

	private int errno = Error.NO_ERROR.getId();
	private String errmsg = Error.NO_ERROR.getMessage();
	private String field;

	public JsonError() {
	}

	public JsonError(Error error) {
		setError(error);
	}

	public void setError(Error e) {
		errno = e.getId();
		errmsg = e.getMessage();
		field = e.getField();
		e.field = null;
	}

	public JsonError(String message) {
		setMessage(message);
	}

	public void setMessage(String message) {
		errno = -1;
		errmsg = message;
	}

	public int getErrno() {
		return errno;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public String getField() {
		return field;
	}

	public JsonError translateErrmsg(Locale locale, MessageSource messageSource) {
		errmsg = messageSource.getMessage(errmsg, null, locale);

		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(errno, errmsg, field);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JsonError)) {
			return false;
		}

		JsonError error = (JsonError) obj;

		return error.errno == errno
				&& error.errmsg.equals(errmsg)
				&& error.field.equals(field);
	}

	@Override
	public String toString() {
		return "{\n" +
				"\terrno: " + errno + ",\n" +
				"\terrmsg: \"" + errmsg + "\",\n" +
				"\tfield: \"" + field + "\"\n" +
				'}';
	}

	public enum Error {
		NO_ERROR(0, "No errors"),
		UNEXPECTED(1, "Unexpected error"),
		ACCESS_DENIED(2, "Access denied"),
		IMAGE_ALREADY_EXISTS(3, "Image already exists"),
		MAP_MARKER_ALREADY_EXISTS(4, "Map marker already exists"),
		USER_ALREADY_EXISTS(5, "User already exists"),
		MISSING_FIELD(6, "Missing field"),
		BAD_FIELD_FORMAT(7, "Bad field format"),
		USER_NOT_EXIST(8, "User with current login doesn't exist"),
		ISSUE_NOT_EXIST(9, "Issue with current id doesn't exist"),
		BAD_CREDENTIALS(9, "wrong_password"),
		RECOVERY_TOKEN_EXPIRED(10, "The recovery token is expired"),
		IMAGE_NOT_EXIST(11, "Image doesn't exist"),
		MAP_MARKER_NOT_EXIST(12, "Map marker doesn't exist"),
		RECOVERY_TOKEN_NOT_EXIST(13, "Recovery token doesn't exist"),
		ILLEGAL_PARAMETER(14, "The retrieved parameter is illegal"),
		TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS(15, "There are too many non expired recovery tokens for the user"),
		USER_BLOCKED_BY_MAX_FAILED_AUTH(16, "The user has been blocked because of failed authentication attempts. Try later"),
		INTERNAL_ERROR(17, "Internal server error"),
		NOT_SUBMITTED_REGISTRATION(18, "Registration is not submitted vie E-Mail"),;

		public static final String NO_ERROR_NAME = "NO_ERROR";
		public static final String UNEXPECTED_NAME = "UNEXPECTED";
		public static final String ACCESS_DENIED_NAME = "ACCESS_DENIED";
		public static final String IMAGE_ALREADY_EXISTS_NAME = "IMAGE_ALREADY_EXISTS";
		public static final String MAP_MARKER_ALREADY_EXISTS_NAME = "MAP_MARKER_ALREADY_EXISTS";
		public static final String USER_ALREADY_EXISTS_NAME = "USER_ALREADY_EXISTS";
		public static final String MISSING_FIELD_NAME = "MISSING_FIELD";
		public static final String BAD_FIELD_FORMAT_NAME = "BAD_FIELD_FORMAT";
		public static final String USER_NOT_EXIST_NAME = "USER_NOT_EXIST";
		public static final String BAD_CREDENTIALS_NAME = "BAD_CREDENTIALS";
		public static final String RECOVERY_TOKEN_EXPIRED_NAME = "RECOVERY_TOKEN_EXPIRED";
		public static final String IMAGE_NOT_EXIST_NAME = "IMAGE_NOT_EXIST";
		public static final String MAP_MARKER_NOT_EXIST_NAME = "MAP_MARKER_NOT_EXIST";
		public static final String RECOVERY_TOKEN_NOT_EXIST_NAME = "RECOVERY_TOKEN_NOT_EXIST";
		public static final String ILLEGAL_PARAMETER_NAME = "ILLEGAL_PARAMETER";
		public static final String TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS_NAME = "TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS";
		public static final String USER_BLOCKED_BY_MAX_FAILED_AUTH_NAME = "USER_BLOCKED_BY_MAX_FAILED_AUTH";
		public static final String INTERNAL_ERROR_NAME = "INTERNAL_ERROR";
		public static final String NOT_SUBMITTED_REGISTRATION_NAME = "NOT_SUBMITTED_REGISTRATION";

		private static final ArrayList<Error> lookup = new ArrayList<>();

		static {
			for (Error err : EnumSet.allOf(Error.class)) {
				lookup.add(err.getId(), err);
			}
		}

		private final int id;
		private final String message;
		private String field;

		Error(int id, String message) {
			this.id = id;
			this.message = message;
		}

		public static Error get(int id) {
			return lookup.get(id);
		}

		public int getId() {
			return id;
		}

		public String getMessage() {
			return message;
		}

		public String getField() {
			return field;
		}

		public Error forField(String field) {
			switch (this) {
				case MISSING_FIELD:
				case BAD_FIELD_FORMAT:
				case ILLEGAL_PARAMETER:
				case USER_NOT_EXIST:
				case USER_ALREADY_EXISTS:
				case BAD_CREDENTIALS:
					this.field = field;
					break;
			}
			return this;
		}
	}
}
