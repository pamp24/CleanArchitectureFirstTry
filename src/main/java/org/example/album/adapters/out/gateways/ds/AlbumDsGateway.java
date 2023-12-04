package org.example.album.adapters.out.gateways.ds;

import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.album.adapters.out.gateways.ds.repositories.AlbumRepository;
import org.example.album.usecases.dtos.AlbumDto;
import org.example.album.usecases.dtos.CreateAlbumDto;
import org.example.album.usecases.models.AlbumRequest;
import org.example.album.usecases.models.AlbumResponse;
import org.example.album.usecases.ports.out.AlbumGateway;
import org.example.auth.usecases.models.PagedResponse;
import org.example.role.domains.RoleName;
import org.example.shared.ApiResponse;
import org.example.shared.exceptions.ResourceNotFoundException;
import org.example.shared.models.utils.AppUtils;
import org.example.shared.security.UserPrincipal;
import org.example.user.adapters.out.gateways.ds.entities.UserEntity;
import org.example.user.adapters.out.gateways.ds.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.example.shared.models.utils.AppConstants.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class AlbumDsGateway implements AlbumGateway {
    private static final String CREATED_AT = "createdAt";

    private static final String ALBUM_STR = "Album";

    private static final String YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION = "You don't have permission to make this operation";
    private final AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    public AlbumDsGateway(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }
    @Override
    public boolean existsByTitle(String title) {
        return albumRepository.existsById(id);
    }
    @Override
    public Optional<AlbumEntity> getById(long id) {
        return albumRepository.findById(id);
    }
    @Override
    public AlbumDto getByTitle(String title) {
        AlbumEntity albumEntity =
                albumRepository.findByCreatedBy(id).orElseThrow(ResourceNotFoundException::new);
        return AlbumDto.builder().build();
    }
    @Override
    public void save(CreateAlbumDto requestModel) {
    }
    @Override
    public ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser) {
        AlbumEntity album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, ID, id));
        UserEntity user = userRepository.getUser(currentUser);
        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            albumRepository.deleteById(id);
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted album"), HttpStatus.OK);
        }
//		throw new AlbumDeleteException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
//		throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
    }

    @Override
    public PagedResponse<AlbumResponse> getAllAlbums(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<AlbumEntity> albums = albumRepository.findAll(pageable);

        if (albums.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), albums.getNumber(), albums.getSize(), albums.getTotalElements(),
                    albums.getTotalPages(), albums.isLast());
        }

        List<AlbumResponse> albumResponses = Arrays.asList(modelMapper.map(albums.getContent(), AlbumResponse[].class));

        return new PagedResponse<>(albumResponses, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(),
                albums.isLast());
    }
    @Override
    public ResponseEntity<AlbumEntity> addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser) {
        UserEntity user = userRepository.getUser(currentUser);

        AlbumEntity album = new AlbumEntity();

        modelMapper.map(albumRequest, album);

        album.setUser(user);
        AlbumEntity newAlbum = albumRepository.save(album);
        return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<AlbumEntity> getAlbum(Long id) {
        AlbumEntity album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, ID, id));
        return new ResponseEntity<>(album, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser) {
        AlbumEntity album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, ID, id));
        UserEntity user = userRepository.getUser(currentUser);
        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            album.setTitle(newAlbum.getTitle());
            AlbumEntity updatedAlbum = albumRepository.save(album);

            AlbumResponse albumResponse = new AlbumResponse();

            modelMapper.map(updatedAlbum, albumResponse);

            return new ResponseEntity<>(albumResponse, HttpStatus.OK);
        }
//		throw new AlbumUpdateException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
//		throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
    }

    @Override
    public PagedResponse<AlbumEntity> getUserAlbums(String username, int page, int size) {
        UserEntity user = userRepository.getUserByName(username);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<AlbumEntity> albums = albumRepository.findByCreatedBy(user.getId(), pageable);

        List<AlbumEntity> content = albums.getNumberOfElements() > 0 ? albums.getContent() : Collections.emptyList();

        return new PagedResponse<>(content, albums.getNumber(), albums.getSize(), albums.getTotalElements(), albums.getTotalPages(), albums.isLast());
    }
}
