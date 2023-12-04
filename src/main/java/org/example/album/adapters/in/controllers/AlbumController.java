package org.example.album.adapters.in.controllers;

import org.example.album.adapters.out.gateways.ds.AlbumDsGateway;
import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.album.usecases.ports.in.AlbumRestUseCase;
import org.example.album.usecases.models.AlbumRequest;
import org.example.album.usecases.models.AlbumResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.photo.usecases.models.PhotoResponse;
import org.example.photo.usecases.ports.in.PhotoRestUseCase;
import org.example.shared.ApiResponse;
import org.example.shared.exceptions.ResponseEntityErrorException;
import org.example.shared.models.utils.AppConstants;
import org.example.shared.models.utils.AppUtils;
import org.example.shared.security.CurrentUser;
import org.example.shared.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
	@Autowired
	private AlbumDsGateway albumDsGateway;
	@Autowired
	private AlbumRestUseCase albumRestUseCase;

	@Autowired
	private PhotoRestUseCase photoService;

	@ExceptionHandler(ResponseEntityErrorException.class)
	public ResponseEntity<ApiResponse> handleExceptions(ResponseEntityErrorException exception) {
		return exception.getApiResponse();
	}

	@GetMapping
	public PagedResponse<AlbumResponse> getAllAlbums(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		AppUtils.validatePageNumberAndSize(page, size);

		return albumDsGateway.getAllAlbums(page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<AlbumEntity> addAlbum(@Valid @RequestBody AlbumRequest albumRequest, @CurrentUser UserPrincipal currentUser) {
		return albumDsGateway.addAlbum(albumRequest, currentUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlbumEntity> getAlbum(@PathVariable(name = "id") Long id) {
		return albumDsGateway.getAlbum(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<AlbumResponse> updateAlbum(@PathVariable(name = "id") Long id, @Valid @RequestBody AlbumRequest newAlbum,
			@CurrentUser UserPrincipal currentUser) {
		return albumDsGateway.updateAlbum(id, newAlbum, currentUser);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteAlbum(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		return albumDsGateway.deleteAlbum(id, currentUser);
	}

	@GetMapping("/{id}/photos")
	public ResponseEntity<PagedResponse<PhotoResponse>> getAllPhotosByAlbum(@PathVariable(name = "id") Long id,
																			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
																			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<PhotoResponse> response = photoService.getAllPhotosByAlbum(id, page, size);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
