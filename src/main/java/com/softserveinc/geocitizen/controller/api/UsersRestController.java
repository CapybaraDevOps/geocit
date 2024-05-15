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

import com.softserveinc.geocitizen.dto.EditUserDTO;
import com.softserveinc.geocitizen.exception.BadFieldFormatException;
import com.softserveinc.geocitizen.exception.IllegalParameterException;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.service.interfaces.IImageService;
import com.softserveinc.geocitizen.service.interfaces.IUserService;
import com.softserveinc.tools.model.JsonResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UsersRestController {

	private final IUserService service;
	private final IImageService imageService;
	private final LocaleResolver localeResolver;

	@Autowired
	public UsersRestController(IUserService service,
	                           IImageService imageService,
	                           LocaleResolver localeResolver) {
		this.service = service;
		this.imageService = imageService;
		this.localeResolver = localeResolver;
	}

	@GetMapping
	public JsonResponse allUsers() {
		return new JsonResponse(service.getAllUsers());
	}

	@GetMapping("/{id}")
	public JsonResponse user(@PathVariable int id) {
		return new JsonResponse(service.getUser(id));
	}

	@GetMapping("/profile/{id}")
	public JsonResponse userProfile(@PathVariable int id) {
		return new JsonResponse(service.getUserProfile(id));
	}

	@PutMapping("/edit")
	public JsonResponse edit(@RequestBody @Valid EditUserDTO dto) {
		service.edit(dto);
		return new JsonResponse(service.getUser(AuthorizedUser.getCurrent().getId()));
	}

	@ApiOperation(hidden = true, value = "Return language for user")
	@GetMapping("/currentLang")
	public JsonResponse currentLang(HttpServletRequest request) {
		return new JsonResponse(localeResolver.resolveLocale(request));
	}

	@GetMapping(value = "/image/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] userImage(@PathVariable("userId") int userId) throws IOException {
		return imageService.getUserImageInByte(userId);
	}

	@PutMapping("/image")
	public JsonResponse issue(@RequestParam("image") MultipartFile image) throws IllegalParameterException, BadFieldFormatException {
		if (image == null) {
			throw new IllegalParameterException("image");
		}
		service.updateImage(imageService.parseImage(image));
		return new JsonResponse(service.getUser(AuthorizedUser.getCurrent().getId()));
	}
}