package org.example.photo.usecases.ports.in;


import org.example.photo.usecases.models.PhotoRequest;
import org.example.photo.usecases.models.PhotoResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.ApiResponse;
import org.example.shared.security.UserPrincipal;

public interface PhotoRestUseCase {

	PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

	PhotoResponse getPhoto(Long id);

	PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

	PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

	ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

	PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);

}