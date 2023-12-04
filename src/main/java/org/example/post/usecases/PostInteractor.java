package org.example.post.usecases;



import org.example.category.adapters.out.gateways.ds.entities.CategoryEntity;
import org.example.category.adapters.out.gateways.ds.repositories.CategoryRepository;
import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.post.usecases.ports.in.PostRestUseCase;

import org.example.post.adapters.out.gateways.ds.repositories.PostRepository;
import org.example.post.usecases.models.PostRequest;
import org.example.post.usecases.models.PostResponse;
import org.example.role.domains.RoleName;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.exceptions.BadRequestException;
import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.exceptions.UnauthorizedException;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.models.utils.AppUtils;
import org.example.shared.security.UserPrincipal;
import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.example.tag.adapters.out.gateways.ds.repositories.TagRepository;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.adapters.out.gateways.ds.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.shared.models.utils.AppConstants.*;


@Service
public class PostInteractor implements PostRestUseCase {
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TagRepository tagRepository;

	@Override
	public PagedResponse<PostEntity> getAllPosts(int page, int size) {
		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<PostEntity> posts = postRepository.findAll(pageable);

		List<PostEntity> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

		return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PagedResponse<PostEntity> getPostsByCreatedBy(String username, int page, int size) {
		validatePageNumberAndSize(page, size);
		UserEntity user = userRepository.getUserByName(username);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
		Page<PostEntity> posts = postRepository.findByCreatedBy(user.getId(), pageable);

		List<PostEntity> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

		return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PagedResponse<PostEntity> getPostsByCategory(Long id, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		CategoryEntity category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, id));

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
		Page<PostEntity> posts = postRepository.findByCategory(category.getId(), pageable);

		List<PostEntity> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

		return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PagedResponse<PostEntity> getPostsByTag(Long id, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TAG, ID, id));

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

		Page<PostEntity> posts = postRepository.findByTags(Collections.singletonList(tag), pageable);

		List<PostEntity> content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

		return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PostEntity updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser) {
		PostEntity post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
		CategoryEntity category = categoryRepository.findById(newPostRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, newPostRequest.getCategoryId()));
		if (post.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			post.setTitle(newPostRequest.getTitle());
			post.setBody(newPostRequest.getBody());
			post.setCategory(category);
			return postRepository.save(post);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this post");

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
		PostEntity post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
		if (post.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			postRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted post");
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this post");

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser) {
		UserEntity user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(USER, ID, 1L));
		CategoryEntity category = categoryRepository.findById(postRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, postRequest.getCategoryId()));

		List<TagEntity> tags = new ArrayList<>(postRequest.getTags().size());

		for (String name : postRequest.getTags()) {
			TagEntity tag = tagRepository.findByName(name);
			tag = tag == null ? tagRepository.save(new TagEntity(name)) : tag;

			tags.add(tag);
		}

		PostEntity post = new PostEntity();
		post.setBody(postRequest.getBody());
		post.setTitle(postRequest.getTitle());
		post.setCategory(category);
		post.setUser(user);
		post.setTags(tags);

		PostEntity newPost = postRepository.save(post);

		PostResponse postResponse = new PostResponse();

		postResponse.setTitle(newPost.getTitle());
		postResponse.setBody(newPost.getBody());
		postResponse.setCategory(newPost.getCategory().getName());

		List<String> tagNames = new ArrayList<>(newPost.getTags().size());

		for (TagEntity tag : newPost.getTags()) {
			tagNames.add(tag.getName());
		}

		postResponse.setTags(tagNames);

		return postResponse;
	}

	@Override
	public PostEntity getPost(Long id) {
		return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
	}

	private void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size < 0) {
			throw new BadRequestException("Size number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}
}
