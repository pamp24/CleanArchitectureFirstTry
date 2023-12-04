package org.example.album.adapters.out.gateways.ds.repositories;



import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

	Page<AlbumEntity> findByCreatedBy(Long userId, Pageable pageable);
}
