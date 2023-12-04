package org.example.tag.usecases;


import org.example.role.domains.RoleName;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.exceptions.UnauthorizedException;
import org.example.shared.models.utils.AppUtils;
import org.example.shared.security.UserPrincipal;
import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.example.tag.usecases.ports.in.TagRestUseCase;
import org.example.tag.adapters.out.gateways.ds.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TagInteractor implements TagRestUseCase {

	@Autowired
	private TagRepository tagRepository;

	@Override
	public PagedResponse<TagEntity> getAllTags(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<TagEntity> tags = tagRepository.findAll(pageable);

		List<TagEntity> content = tags.getNumberOfElements() == 0 ? Collections.emptyList() : tags.getContent();

		return new PagedResponse<>(content, tags.getNumber(), tags.getSize(), tags.getTotalElements(), tags.getTotalPages(), tags.isLast());
	}

	@Override
	public TagEntity getTag(Long id) {
		return tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
	}

	@Override
	public TagEntity addTag(TagEntity tag, UserPrincipal currentUser) {
		return tagRepository.save(tag);
	}

	@Override
	public TagEntity updateTag(Long id, TagEntity newTag, UserPrincipal currentUser) {
		TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		if (tag.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
				.contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			tag.setName(newTag.getName());
			return tagRepository.save(tag);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this tag");

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ApiResponse deleteTag(Long id, UserPrincipal currentUser) {
		TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		if (tag.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
				.contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			tagRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted tag");
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this tag");

		throw new UnauthorizedException(apiResponse);
	}
}






















