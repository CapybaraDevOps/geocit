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

package com.softserveinc.geocitizen.controller.system;

import com.softserveinc.geocitizen.exception.*;
import com.softserveinc.geocitizen.security.exception.TooManyNonExpiredRecoveryTokensException;
import com.softserveinc.tools.model.JsonError;
import com.softserveinc.tools.model.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 3:45 PM
 */
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

	private final MessageSource messageSource;
	private final Function<FieldError, JsonError> mapFieldError = fieldError ->
			new JsonError(JsonError.Error.valueOf(fieldError.getDefaultMessage()).forField(fieldError.getField()));

	@Autowired
	public ExceptionHandlerControllerAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = TooManyNonExpiredRecoveryTokensException.class)
	public JsonResponse tooManyNonExpiredRecoveryTokensException(
			TooManyNonExpiredRecoveryTokensException e, Locale locale) {
		logger.error("TooManyNonExpiredRecoveryTokensException", e);
		return new JsonResponse(JsonError.Error.TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS, locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalParameterException.class)
	public JsonResponse illegalParameterException(IllegalParameterException e, Locale locale) {
		logger.error("IllegalParameterException", e);
		return new JsonResponse(e.getError(), locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = ExpiredRecoveryTokenException.class)
	public JsonResponse expiredRecoveryTokenException(ExpiredRecoveryTokenException e, Locale locale) {
		logger.error("ExpiredRecoveryTokenException", e);
		return new JsonResponse(e.getError(), locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public JsonResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, Locale locale) {
		logger.error("MethodArgumentNotValidException", e);
		return new JsonResponse(
				e.getBindingResult().getFieldErrors().stream()
						.map(mapFieldError)
						.map((JsonError error) -> error.translateErrmsg(locale, messageSource))
						.distinct()
						.collect(Collectors.toList()));
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = EntityNotUniqueException.class)
	public JsonResponse entityNotUniqueExceptionHandler(EntityNotUniqueException e, Locale locale) {
		logger.error("EntityNotUniqueException", e);
		return new JsonResponse(e.getError(), locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = BadFieldFormatException.class)
	public JsonResponse badParameterFormatExceptionHandler(BadFieldFormatException e, Locale locale) {
		logger.error("BadFieldFormatException", e);
		return new JsonResponse(e.getError(), locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public JsonResponse illegalArgumentExceptionHandler(IllegalArgumentException e, Locale locale) {
		logger.error("IllegalArgumentException", e);
		return new JsonResponse(new JsonError("Internal error").translateErrmsg(locale, messageSource));
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public JsonResponse missingRequestParameterExceptionHandler(
			MissingServletRequestParameterException e, Locale locale) {
		logger.error("MissingServletRequestParameterException", e);
		return new JsonResponse(JsonError.Error.MISSING_FIELD.forField(e.getParameterName()), locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(value = AccessDeniedException.class)
	public JsonResponse accessDeniedExceptionHandler(AccessDeniedException e, Locale locale) {
		logger.error("AccessDeniedException", e);
		return new JsonResponse(JsonError.Error.ACCESS_DENIED, locale, messageSource);
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public JsonResponse generalExceptionHandler(Exception e, Locale locale) {
		logger.error(e.getClass().getName(), e);
		return new JsonResponse(JsonError.Error.INTERNAL_ERROR, locale, messageSource);
	}
}
