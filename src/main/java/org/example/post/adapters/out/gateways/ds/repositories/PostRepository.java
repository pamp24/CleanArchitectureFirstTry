package org.example.post.adapters.out.gateways.ds.repositories;



import org.example.post.adapters.out.gateways.ds.entities.PostEntity;
import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
	Page<PostEntity> findByCreatedBy(Long userId, Pageable pageable);

	Page<PostEntity> findByCategory(Long categoryId, Pageable pageable);

	Page<PostEntity> findByTags(List<TagEntity> tags, Pageable pageable);

	Long countByCreatedBy(Long userId);
}
