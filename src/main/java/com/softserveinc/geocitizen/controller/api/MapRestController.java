/*
 * The following code have been created by Yurii Kiziuk.
 * The code can be used in non-commercial way.
 * For any commercial use it needs an author's agreement.
 * Please contact the author:
 *  - yurakizyuk@gmail.com
 *
 * Copyright (c) 2017 by Yurii Kiziuk.
 */

package com.softserveinc.geocitizen.controller.api;

import com.softserveinc.geocitizen.dto.MapDataDTO;
import com.softserveinc.geocitizen.entity.MapMarker;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;
import com.softserveinc.geocitizen.service.interfaces.IIssueService;
import com.softserveinc.geocitizen.service.interfaces.IMapMarkersService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/map")
public class MapRestController {

	private final IMapMarkersService markerService;
	private final IIssueService issueService;

	@Autowired
	public MapRestController(IMapMarkersService markerService,
	                         IIssueService issueService) {
		this.markerService = markerService;
		this.issueService = issueService;
	}

	@GetMapping
	public JsonResponse allMarkers() {
		return new JsonResponse(markerService.loadAllMarkers());
	}

	@GetMapping("/marker/{lat}/{lng}/")
	public JsonResponse markerByCoords(@PathVariable("lat") double lat,
	                                   @PathVariable("lng") double lng) {
		return new JsonResponse(markerService.getMarker(lat, lng));
	}

	@PostMapping("/marker")
	public JsonResponse marker(@RequestBody final MapMarker marker) {
		return new JsonResponse(markerService.saveMarker(marker));
	}

//	@PostMapping("/issue")
//	public JsonResponse issue(@RequestParam(value = "file", required = false) MultipartFile image,
//	                          @Valid @ModelAttribute MapDataDTO dto) throws AbstractCitizenException {
//		if (image == null) {
//			throw new IllegalParameterException("file");
//		}
//		return new JsonResponse(issueService.saveIssue(dto, image));
//	}

	@PostMapping("/issue")
	public JsonResponse issue(
//			@RequestParam(value = "file", required = false) MultipartFile image,
			@Valid @RequestBody MapDataDTO dto) throws AbstractCitizenException {
		return new JsonResponse(issueService.saveIssue(dto));
	}

	@GetMapping("/issues/mapMarker/{mapMarkerId}")
	public JsonResponse issueByMapMarker(@PathVariable("mapMarkerId") int mapMarkerId) {
		return new JsonResponse(issueService.getAllIssueByMapMarker(mapMarkerId));
	}
}