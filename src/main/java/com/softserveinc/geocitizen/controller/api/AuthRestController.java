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

package com.softserveinc.geocitizen.controller.api;

import com.softserveinc.geocitizen.dto.PasswordRecoveryDTO;
import com.softserveinc.geocitizen.dto.RegisterUserDTO;
import com.softserveinc.geocitizen.dto.SubmitRegistrationDTO;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;
import com.softserveinc.geocitizen.service.interfaces.IAuthService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/20/17 at 5:59 PM
 */
@RestController
@RequestMapping("/auth")
public class AuthRestController {

	private final IAuthService service;

	@Autowired
	public AuthRestController(IAuthService service) {
		this.service = service;
	}

	@PostMapping("/requestRecoveryToken")
	public JsonResponse generateRecoveryToken(@RequestParam(name = "login") String login, HttpServletRequest request)
			throws AbstractCitizenException, MessagingException {
		return new JsonResponse(service.generateRecoveryToken(login, request.getRemoteAddr()));
	}

	@GetMapping("/getCurrentSession")
	public JsonResponse currentSession() {
		return new JsonResponse(service.getCurrentSession());
	}

	@PostMapping("/recoverPassword")
	public JsonResponse recoverPassword(@RequestBody @Valid PasswordRecoveryDTO dto) throws AbstractCitizenException {
		return new JsonResponse(service.recoverPassword(dto));
	}

	@PostMapping("/signUp")
	public JsonResponse signUp(@RequestBody @Valid RegisterUserDTO dto)
			throws AbstractCitizenException, MessagingException {
		return new JsonResponse(service.signUp(dto));
	}

	@PostMapping("/submitSignUp")
	public JsonResponse submitSignUp(@RequestBody @Valid SubmitRegistrationDTO dto) throws AbstractCitizenException {
		service.submitSignUp(dto);
		return JsonResponse.Builder.aJsonResponse().build();
	}

	@PostMapping("/update")
	public JsonResponse updateUser(@RequestBody @Valid RegisterUserDTO user) throws AbstractCitizenException {
		return new JsonResponse(service.update(user));
	}
}
