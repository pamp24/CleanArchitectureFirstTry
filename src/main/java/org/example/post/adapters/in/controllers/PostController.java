package org.example.post.adapters.in.controllers;


import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.post.usecases.ports.in.PostRestUseCase;
import org.example.post.usecases.models.PostRequest;
import org.example.post.usecases.models.PostResponse;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.security.CurrentUser;
import org.example.shared.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	@Autowired
	private PostRestUseCase postService;

	@GetMapping
	public ResponseEntity<PagedResponse<PostEntity>> getAllPosts(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<PostEntity> response = postService.getAllPosts(page, size);

		return new ResponseEntity< >(response, HttpStatus.OK);
	}

	@GetMapping("/org/example/category/{id}")
	public ResponseEntity<PagedResponse<PostEntity>> getPostsByCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long id) {
		PagedResponse<PostEntity> response = postService.getPostsByCategory(id, page, size);

		return new ResponseEntity< >(response, HttpStatus.OK);
	}

	@GetMapping("/org/example/tag/{id}")
	public ResponseEntity<PagedResponse<PostEntity>> getPostsByTag(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long id) {
		PagedResponse<PostEntity> response = postService.getPostsByTag(id, page, size);

		return new ResponseEntity< >(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<PostResponse> addPost(@Valid @RequestBody PostRequest postRequest,
												@CurrentUser UserPrincipal currentUser) {
		PostResponse postResponse = postService.addPost(postRequest, currentUser);

		return new ResponseEntity< >(postResponse, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostEntity> getPost(@PathVariable(name = "id") Long id) {
		PostEntity post = postService.getPost(id);

		return new ResponseEntity< >(post, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<PostEntity> updatePost(@PathVariable(name = "id") Long id,
			@Valid @RequestBody PostRequest newPostRequest, @CurrentUser UserPrincipal currentUser) {
		PostEntity post = postService.updatePost(id, newPostRequest, currentUser);
		return new ResponseEntity< >(post, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = postService.deletePost(id, currentUser);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}
}
