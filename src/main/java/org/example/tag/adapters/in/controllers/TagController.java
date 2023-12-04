package org.example.tag.adapters.in.controllers;


import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.security.CurrentUser;
import org.example.shared.security.UserPrincipal;
import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.example.tag.usecases.ports.in.TagRestUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tags")
public class TagController {
	@Autowired
	private TagRestUseCase tagService;

	@GetMapping
	public ResponseEntity<PagedResponse<TagEntity>> getAllTags(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		PagedResponse<TagEntity> response = tagService.getAllTags(page, size);

		return new ResponseEntity< >(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<TagEntity> addTag(@Valid @RequestBody TagEntity tag, @CurrentUser UserPrincipal currentUser) {
		TagEntity newTag = tagService.addTag(tag, currentUser);

		return new ResponseEntity< >(newTag, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TagEntity> getTag(@PathVariable(name = "id") Long id) {
		TagEntity tag = tagService.getTag(id);

		return new ResponseEntity< >(tag, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<TagEntity> updateTag(@PathVariable(name = "id") Long id, @Valid @RequestBody TagEntity tag, @CurrentUser UserPrincipal currentUser) {

		TagEntity updatedTag = tagService.updateTag(id, tag, currentUser);

		return new ResponseEntity< >(updatedTag, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteTag(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = tagService.deleteTag(id, currentUser);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

}
