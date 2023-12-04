package org.example.album.usecases.ports.in;


import org.example.album.adapters.out.gateways.ds.entities.AlbumEntity;
import org.example.album.adapters.out.presenters.view.resources.CreateAlbumResponseResources;
import org.example.album.usecases.models.AlbumRequest;
import org.example.album.usecases.models.AlbumResponse;
import org.example.album.usecases.models.CreateAlbumRequestModel;
import org.example.album.usecases.models.CreateAlbumResponseModel;
import org.example.shared.ApiResponse;
import org.example.auth.usecases.models.PagedResponse;
import org.example.shared.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface AlbumRestUseCase {


	CreateAlbumResponseResources create(CreateAlbumRequestModel createAlbumRequestModel);
}
