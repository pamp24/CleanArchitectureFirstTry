package org.example.tag.adapters.out.gateways.ds.repositories;


import org.example.tag.adapters.out.gateways.ds.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
	TagEntity findByName(String name);
}
