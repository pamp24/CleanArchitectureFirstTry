package org.example.album.usecases.ports.out;



import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.album.usecases.dtos.AlbumDto;
import org.example.album.usecases.dtos.CreateAlbumDto;
import org.example.album.usecases.models.AlbumRequest;
import org.example.album.usecases.models.AlbumResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.ApiResponse;
import org.example.shared.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AlbumGateway {
    boolean existsByTitle(String title);
    Optional<AlbumEntity> getById(long id);
    AlbumDto getByTitle(String title);
    void save(CreateAlbumDto requestModel);
    ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser);
    PagedResponse<AlbumResponse> getAllAlbums(int page, int size);
    ResponseEntity<AlbumEntity> getAlbum(Long id);
    ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser);
    ResponseEntity<AlbumEntity> addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser);

    PagedResponse<AlbumEntity> getUserAlbums(String username, int page, int size);
}
