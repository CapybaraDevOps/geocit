package com.softserveinc.geocitizen.controller.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FailedAccessSocialController {

	@Value("${front.url}")
	private String frontUrl;

	@GetMapping(value = "/signin", params = {"error"})
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect(frontUrl + "/auth");
	}
}
