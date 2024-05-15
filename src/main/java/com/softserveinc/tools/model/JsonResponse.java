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

import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0 Created 12/20/17 at 1:10 AM
 */
public class JsonResponse {

	private List<JsonError> errors;
	private List<Object> data;
	private Long count = 0l;

	private JsonResponse() {
	}

	public JsonResponse(Object data) {
		setData(data);
	}

	public JsonResponse(JsonError.Error error) {
		addError(error);
	}

	private void addError(JsonError.Error error) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		this.errors.add(new JsonError(error));

		count = null;
	}

	public JsonResponse(JsonError.Error error, Locale locale, MessageSource messageSource) {
		addError(error, locale, messageSource);
	}

	private void addError(JsonError.Error error, Locale locale, MessageSource messageSource) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		this.errors.add(new JsonError(error).translateErrmsg(locale, messageSource));

		count = null;
	}

	public JsonResponse(JsonError jsonError) {
		errors = new ArrayList<>();

		errors.add(jsonError);

		count = null;
	}

	public JsonResponse(List<JsonError> errors) {
		setErrors(errors);
	}

	public JsonResponse(List<JsonError> errors, Locale locale, MessageSource messageSource) {
		setErrors(errors, locale, messageSource);
	}

	private void setErrors(List<JsonError> errors, Locale locale, MessageSource messageSource) {
		this.errors = new ArrayList<>();
		count = null;

		this.errors.addAll(errors.stream().map(jErr -> jErr.translateErrmsg(locale, messageSource))
				.collect(Collectors.toList()));
	}

	public List<JsonError> getErrors() {
		return errors;
	}

	private void setErrors(List<JsonError> errors) {
		this.errors = new ArrayList<>();
		count = null;

		this.errors.addAll(errors);
	}

	public List<Object> getData() {
		return data;
	}

	private void setData(Object data) {
		this.data = new ArrayList<>();

		if (data instanceof Collection) {
			this.data.addAll((Collection) data);
		} else {
			this.data.add(data);
		}

		count = Long.valueOf(this.data.size());
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "JsonResponse{" +
				"errors=" + errors +
				", data=" + data +
				", count=" + count +
				'}';
	}

	public static final class Builder {

		private JsonResponse jsonResponse;

		private Builder() {
			jsonResponse = new JsonResponse();
		}

		public static Builder aJsonResponse() {
			return new Builder();
		}

		public Builder setData(Object data) {
			jsonResponse.setData(data);
			return this;
		}

		public Builder withError(JsonError.Error error) {
			jsonResponse.addError(error);
			return this;
		}

		public Builder setErrors(List<JsonError> errors) {
			jsonResponse.setErrors(errors);
			return this;
		}

		public Builder setCount(Long count) {
			jsonResponse.setCount(count);
			return this;
		}

		public JsonResponse build() {
			return jsonResponse;
		}
	}
}
