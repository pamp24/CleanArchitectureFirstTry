package org.example.photo.adapters.out.gateways.ds.repositories;



import org.example.photo.adapters.out.gateways.ds.entities.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
	Page<PhotoEntity> findByAlbumId(Long albumId, Pageable pageable);
}
