package com.softserveinc.geocitizen.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserveinc.geocitizen.service.interfaces.IAuthService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/27/17 at 12:49 AM
 */
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
		implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private final IAuthService authService;

	@Autowired
	public LogoutSuccessHandler(
			IAuthService authService) {
		this.authService = authService;
	}

	@Override
	public void onLogoutSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpServletResponse.SC_OK);
		MAPPER.writeValue(response.getWriter(), new JsonResponse(authService.getCurrentSession()));
	}
}
