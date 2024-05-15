package com.softserveinc.geocitizen.controller.api.admin;

import com.softserveinc.geocitizen.service.interfaces.IMsgService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class MsgManagementController {

	private final IMsgService msgService;

	@Autowired
	public MsgManagementController(IMsgService msgService) {
		this.msgService = msgService;
	}

	@GetMapping("/fuckoff")
	public JsonResponse getAll() {
		return new JsonResponse(msgService.findAll());
	}

	@RequestMapping(value = "/mmm/{id}/{text}", method = RequestMethod.POST)
	public JsonResponse save(@PathVariable String id, @PathVariable String text) {
		return new JsonResponse(msgService.save(id, text));




	}

}


