package org.example.comment.adapters.in.controllers;


import org.example.auth.usecases.models.PagedResponse;
import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.example.comment.usecases.models.CommentRequest;
import org.example.comment.usecases.ports.in.CommentRestUseCase;
import org.example.shared.ApiResponse;
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
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
	@Autowired
	private CommentRestUseCase commentRestUseCase;

	@GetMapping
	public ResponseEntity<PagedResponse<CommentEntity>> getAllComments(@PathVariable(name = "postId") Long postId,
																	   @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
																	   @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<CommentEntity> allComments = commentRestUseCase.getAllComments(postId, page, size);

		return new ResponseEntity< >(allComments, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CommentEntity> addComment(@Valid @RequestBody CommentRequest commentRequest,
			@PathVariable(name = "postId") Long postId, @CurrentUser UserPrincipal currentUser) {
		CommentEntity newComment = commentRestUseCase.addComment(commentRequest, postId, currentUser);

		return new ResponseEntity<>(newComment, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CommentEntity> getComment(@PathVariable(name = "postId") Long postId,
			@PathVariable(name = "id") Long id) {
		CommentEntity comment = commentRestUseCase.getComment(postId, id);

		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<CommentEntity> updateComment(@PathVariable(name = "postId") Long postId,
			@PathVariable(name = "id") Long id, @Valid @RequestBody CommentRequest commentRequest,
			@CurrentUser UserPrincipal currentUser) {

		CommentEntity updatedComment = commentRestUseCase.updateComment(postId, id, commentRequest, currentUser);

		return new ResponseEntity<>(updatedComment, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable(name = "postId") Long postId,
													 @PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {

		ApiResponse response = commentRestUseCase.deleteComment(postId, id, currentUser);

		HttpStatus status = response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(response, status);
	}

}
