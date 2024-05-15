package com.softserveinc.geocitizen.controller.api;

import com.softserveinc.geocitizen.exception.BadFieldFormatException;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.service.interfaces.IImageService;
import com.softserveinc.geocitizen.service.interfaces.IIssueService;
import com.softserveinc.geocitizen.service.interfaces.IIssueTypesService;
import com.softserveinc.geocitizen.service.interfaces.IIssueVotesService;
import com.softserveinc.tools.model.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/issues")
public class IssueRestController {

	private static final String LIKE = "likeVote";
	private static final String DISLIKE = "dislikeVote";

	private final IIssueService issueService;
	private final IIssueVotesService issueVotesService;
	private final IIssueTypesService issueTypesService;
	private final IImageService imageService;

	@Autowired
	public IssueRestController(IIssueService issueService,
	                           IIssueVotesService issueVotesService,
	                           IIssueTypesService issueTypesService,
	                           IImageService imageService) {
		this.issueService = issueService;
		this.issueTypesService = issueTypesService;
		this.issueVotesService = issueVotesService;
		this.imageService = imageService;
	}

	@GetMapping
	public JsonResponse all(/*@PageableDefault(page=0,size=10,sort="title")Pageable pageable*/
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size
	) {
		return new JsonResponse(issueService.findAll(new PageRequest(page, size)));
	}

	@GetMapping("/types")
	public JsonResponse allTypes() {
		return new JsonResponse(issueTypesService.getAll());
	}

	@GetMapping("/{issueId}")
	public JsonResponse issue(@PathVariable("issueId") Integer issueId) {
		return new JsonResponse(issueService.getById(issueId));
	}

	@GetMapping("/{issueId}/is-vote-exist")
	public JsonResponse voteExists(@PathVariable("issueId") Integer issueId) {
		Boolean vote = issueVotesService
				.getByVoterIdAndIssueId(AuthorizedUser.getCurrent().getId(), issueId).getVote();
		return new JsonResponse(vote);
	}

	@DeleteMapping("/{issueId}/vote")
	public void vote(@PathVariable("issueId") Integer issueId) {
		issueVotesService.deleteByVoterIdAndIssueId(AuthorizedUser.getCurrent().getId(), issueId);
	}

	@PostMapping("/{issueId}/{vote}")
	public void vote(@PathVariable("issueId") Integer issueId,
	                 @PathVariable("vote") Boolean vote) {
		issueVotesService.insertIssueVote(issueId, AuthorizedUser.getCurrent().getId(), vote);
	}

	@GetMapping("/{issueId}/votes")
	public JsonResponse calculateVotes(@PathVariable("issueId") Integer issueId) {
		Map<String, Long> map = new HashMap<>();
		map.put(LIKE, issueVotesService.countByVoteAndIssue(true, issueId));
		map.put(DISLIKE, issueVotesService.countByVoteAndIssue(false, issueId));
		return new JsonResponse(map);
	}

	@GetMapping(value = "/images/{issueId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] issueImage(@PathVariable("issueId") Integer issueId) throws IOException {
		return imageService.getIssueImageInByte(issueId);
	}

	@PutMapping(value = "/{issueId}"/*, params = "file != null"*/)
	public JsonResponse uploadImage(@PathVariable("issueId") Integer issueId,
	                                @RequestParam(value = "file") MultipartFile image) throws BadFieldFormatException {
		return new JsonResponse(issueService.setImage(issueId, image));
	}
}
