package com.softserveinc.geocitizen.controller.api.admin;

import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;
import com.softserveinc.geocitizen.service.interfaces.IUserService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class UserManagementController {

	private final IUserService userService;

	@Autowired
	public UserManagementController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public JsonResponse getAll(@PageableDefault(page = 0, size = 10, sort = "login") Pageable pageable) {
		Page<User> users = userService.findAll(pageable);
		return JsonResponse.Builder.aJsonResponse()
				.setData(users.getContent())
				.setCount(users.getTotalElements())
				.build();
	}

	@GetMapping("/{id}")
	public JsonResponse getById(@PathVariable Integer id) throws AbstractCitizenException {
		return new JsonResponse(userService.findById(id));
	}

	@GetMapping("/login/{login}")
	public JsonResponse getByLogin(@PathVariable String login) throws AbstractCitizenException {
		return new JsonResponse(userService.findByLogin(login));
	}

	@GetMapping("/search/{query}")
	public JsonResponse getByLoginOrEmailOrNameOrSurname(@PathVariable String query,
	                                                     @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
		Page<User> users = userService.findByLoginOrEmailOrNameOrSurname(query, query, query, query, pageable);
		return JsonResponse.Builder.aJsonResponse()
				.setData(users.getContent())
				.setCount(users.getTotalElements())
				.build();
	}

	@PutMapping("/{id}/{type}")
	public JsonResponse setUserStatus(@PathVariable int id,
	                                  @PathVariable String type) throws AbstractCitizenException {
		return new JsonResponse(userService.setUserStatus(User.Type.valueOf(type.toUpperCase()), id));
	}

	@GetMapping("/type/{type}")
	public JsonResponse getUsersByStatus(@PathVariable String type,
	                                     @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
		Page<User> users = userService.findByType(User.Type.valueOf(type.toUpperCase()), pageable);
		return JsonResponse.Builder.aJsonResponse()
				.setData(users.getContent())
				.setCount(users.getTotalElements())
				.build();
	}
}
