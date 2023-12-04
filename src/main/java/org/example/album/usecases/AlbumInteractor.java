package org.example.album.usecases;

import org.example.album.adapters.out.gateways.ds.AlbumDsGateway;
import org.example.album.adapters.out.presenters.view.resources.CreateAlbumResponseResources;
import org.example.album.domains.Album;
import org.example.album.usecases.dtos.AlbumDto;
import org.example.album.usecases.dtos.CreateAlbumDto;
import org.example.album.usecases.models.CreateAlbumRequestModel;
import org.example.album.usecases.models.CreateAlbumResponseModel;
import org.example.album.usecases.ports.in.AlbumRestUseCase;
import org.example.album.usecases.ports.out.AlbumPresenter;
import org.springframework.stereotype.Service;


@Service
public class AlbumInteractor implements AlbumRestUseCase { //AlbumInteractor

	private final AlbumDsGateway albumDsGateway;
	private final AlbumPresenter albumPresenter;

	public AlbumInteractor(AlbumDsGateway albumDsGateway, AlbumPresenter albumPresenter){
		this.albumDsGateway = albumDsGateway;
		this.albumPresenter = albumPresenter;
	}
	@Override
	public CreateAlbumResponseResources create(CreateAlbumRequestModel createAlbumRequestModel){
		if (albumDsGateway.existsByTitle(createAlbumRequestModel.getTitle())) {
			return albumPresenter.albumDeleteException();
		}
		if (albumDsGateway.existsByTitle(createAlbumRequestModel.getTitle())){
			return albumPresenter.albumDeleteException();
		}

		Album album = Album.builder()
						.title(createAlbumRequestModel.getTitle())
						.build();

		CreateAlbumDto createAlbumDto =
				CreateAlbumDto.builder()
						.title(album.gettitle())
						.build();

		albumDsGateway.save(createAlbumDto);

		AlbumDto albumDto = albumDsGateway.getByTitle(album.gettitle());

		CreateAlbumResponseModel createAlbumResponseModel =
				CreateAlbumResponseModel.builder()
						.title(albumDto.getTitle())
						.build();

		return albumPresenter.successResponse(createAlbumResponseModel);
	}

}