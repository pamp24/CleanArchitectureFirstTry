package org.example.comment.adapters.out.gateways.ds.repositories;


import org.example.comment.adapters.out.gateways.ds.entities.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
}
