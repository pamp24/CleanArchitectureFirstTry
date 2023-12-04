package org.example.comment.usecases.ports.in;


import org.example.auth.usecases.models.PagedResponse;

import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.example.comment.usecases.models.CommentRequest;
import org.example.shared.ApiResponse;
import org.example.shared.security.UserPrincipal;

public interface CommentRestUseCase {

	PagedResponse<CommentEntity> getAllComments(Long postId, int page, int size);

	CommentEntity addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser);

	CommentEntity getComment(Long postId, Long id);

	CommentEntity updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser);

	ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);

}
