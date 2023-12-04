package org.example.comment.usecases;

import org.example.auth.usecases.models.PagedResponse;
import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.example.comment.usecases.ports.in.CommentRestUseCase;
import org.example.comment.adapters.out.gateways.ds.repositories.CommentRepository;
import org.example.comment.usecases.models.CommentRequest;
import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.post.adapters.out.gateways.ds.repositories.PostRepository;
import org.example.role.domains.RoleName;
import org.example.shared.ApiResponse;
import org.example.shared.exceptions.BlogapiException;
import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.models.utils.AppUtils;
import org.example.shared.security.UserPrincipal;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.adapters.out.gateways.ds.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Service;

@Service
public class CommentInteractor implements CommentRestUseCase {
	private static final String THIS_COMMENT = " this comment";

	private static final String YOU_DON_T_HAVE_PERMISSION_TO = "You don't have permission to ";

	private static final String ID_STR = "id";

	private static final String COMMENT_STR = "Comment";

	private static final String POST_STR = "Post";

	private static final String COMMENT_DOES_NOT_BELONG_TO_POST = "Comment does not belong to post";

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public PagedResponse<CommentEntity> getAllComments(Long postId, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<CommentEntity> comments = commentRepository.findByPostId(postId, pageable);

		return new PagedResponse<>(comments.getContent(), comments.getNumber(), comments.getSize(),
				comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
	}

	@Override
	public CommentEntity addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
		UserEntity user = userRepository.getUser(currentUser);
		CommentEntity comment = new CommentEntity(commentRequest.getBody());
		comment.setUser(user);
		comment.setPost(post);
		comment.setName(currentUser.getUsername());
		comment.setEmail(currentUser.getEmail());
		return commentRepository.save(comment);
	}

	@Override
	public CommentEntity getComment(Long postId, Long id) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
		CommentEntity comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));
		if (comment.getPost().getId().equals(post.getId())) {
			return comment;
		}

		throw new BlogapiException(HttpStatus.BAD_REQUEST, COMMENT_DOES_NOT_BELONG_TO_POST);
	}

	@Override
	public CommentEntity updateComment(Long postId, Long id, CommentRequest commentRequest,
			UserPrincipal currentUser) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
		CommentEntity comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

		if (!comment.getPost().getId().equals(post.getId())) {
			throw new BlogapiException(HttpStatus.BAD_REQUEST, COMMENT_DOES_NOT_BELONG_TO_POST);
		}

		if (comment.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			comment.setBody(commentRequest.getBody());
			return commentRepository.save(comment);
		}

		throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO + "update" + THIS_COMMENT);
	}

	@Override
	public ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
		CommentEntity comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

		if (!comment.getPost().getId().equals(post.getId())) {
			return new ApiResponse(Boolean.FALSE, COMMENT_DOES_NOT_BELONG_TO_POST);
		}

		if (comment.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			commentRepository.deleteById(comment.getId());
			return new ApiResponse(Boolean.TRUE, "You successfully deleted comment");
		}

		throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO + "delete" + THIS_COMMENT);
	}
}
