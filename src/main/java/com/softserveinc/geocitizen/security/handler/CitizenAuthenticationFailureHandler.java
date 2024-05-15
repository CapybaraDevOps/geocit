package com.softserveinc.geocitizen.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.geocitizen.security.exception.interfaces.ICitizenAuthenticationException;
import com.softserveinc.tools.model.JsonError;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/3/18 at 3:30 PM
 */
@Component
public class CitizenAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final MessageSource messageSource;
	private final LocaleResolver localeResolver;

	@Autowired
	public CitizenAuthenticationFailureHandler(MessageSource messageSource, LocaleResolver localeResolver) {
		this.messageSource = messageSource;
		this.localeResolver = localeResolver;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException e
	) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

		if (ICitizenAuthenticationException.class.isAssignableFrom(e.getClass())) {
			logger.error(e.getClass().getName(), e);
			MAPPER.writeValue(response.getWriter(), new JsonResponse(((ICitizenAuthenticationException) e).getError(),
					localeResolver.resolveLocale(request), messageSource));
		} else {
			logger.error("AuthenticationException", e);
			MAPPER.writeValue(response.getWriter(), new JsonResponse(new JsonError(
					e.getMessage()).translateErrmsg(localeResolver.resolveLocale(request), messageSource)));
		}
	}
}
