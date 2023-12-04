package org.example.tag.usecases.ports.in;


import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.security.UserPrincipal;
import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;

public interface TagRestUseCase {

	PagedResponse<TagEntity> getAllTags(int page, int size);

	TagEntity getTag(Long id);

	TagEntity addTag(TagEntity tag, UserPrincipal currentUser);

	TagEntity updateTag(Long id, TagEntity newTag, UserPrincipal currentUser);

	ApiResponse deleteTag(Long id, UserPrincipal currentUser);

}
