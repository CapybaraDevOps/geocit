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

package com.softserveinc.geocitizen.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.geocitizen.service.interfaces.IAuthService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 3:15 PM
 */
@Component
public class CitizenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final IAuthService authService;

	@Autowired
	public CitizenAuthenticationSuccessHandler(IAuthService authService) {
		this.authService = authService;
	}

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException {
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpServletResponse.SC_OK);
		MAPPER.writeValue(response.getWriter(), new JsonResponse(authService.getCurrentSession()));
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
